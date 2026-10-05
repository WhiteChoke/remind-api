package com.whitechoke.remind.storage;

import com.whitechoke.remind.storage.fileOutbox.FileOutboxEntity;
import com.whitechoke.remind.storage.fileOutbox.FileOutboxRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {
    private final FileOutboxRepository fileOutboxRepository;
    private final Path root = Paths.get("saved");
    private static final Pattern SAFE_EXT = Pattern.compile("[a-zA-Z0-9]{1,10}");

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectory(root);
        for (FileType fileType : FileType.values()) {
            Files.createDirectory(Paths.get(root.toString(), fileType.toString()));
        }
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public Path save(
            MultipartFile file,
            FileType type
    ) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        var filename = UUID.randomUUID() + extensionOf(file);
        var dir = root.resolve(type.name()).normalize();
        var target = dir.resolve(filename).normalize();

        if (!target.startsWith(root.normalize())) {
            throw new SecurityException("Invalid target path");
        }

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            fileOutboxRepository.save(
                FileOutboxEntity.builder()
                        .filePath(target.toString())
                        .createdAt(Instant.now())
                        .build()
            );
        } catch (IOException e) {
            log.error("Failed to save file: {}", e.getMessage());

            try {
                Files.deleteIfExists(target);
            } catch (IOException ex) {
            // may be bad files
                e.addSuppressed(ex);
            }

            throw new RuntimeException(e);
        }

        return target;
    }

    @Transactional
    public <T extends Storable> T saveToDb(
            Path path,
            JpaRepository<T, ?> repository,
            Supplier<T> factory
    ) {
        var entity = factory.get();
        entity.setPath(path.toString());

        return repository.save(entity);
    }

    private String extensionOf(MultipartFile file) {
        var ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        return ext != null && SAFE_EXT.matcher(ext).matches() ? "." + ext.toLowerCase() : "";
    }

    public boolean deleteFile(Path path) {
        if (!Files.exists(path)) {
            return true;
        }

        try {
            Files.delete(path);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage());
            return false;
        }

        return true;
    }
}
