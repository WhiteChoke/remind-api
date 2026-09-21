package com.whitechoke.remind.storage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {
    private final Path root = Paths.get("saved");
    private static final Pattern SAFE_EXT = Pattern.compile("[a-zA-Z0-9]{1,10}");


    @PostConstruct
    public void init() throws IOException {
        Files.createDirectory(root);
    }

    public <T extends Storable> T save(
            MultipartFile file,
            Supplier<T> factory,
            JpaRepository<T, ?> repository,
            FileType type
    ) throws IOException {
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
        }

        try {
            var entity = factory.get();
            entity.setPath(type.name() + "/" + filename);
            return repository.save(entity);
        } catch (RuntimeException e) {
            try {
                Files.deleteIfExists(target);
            } catch (IOException ex) {
                e.addSuppressed(ex);
            }
            throw e;
        }

    }

    private String extensionOf(MultipartFile file) {
        var ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        return ext != null && SAFE_EXT.matcher(ext).matches() ? "." + ext.toLowerCase() : "";
    }
}
