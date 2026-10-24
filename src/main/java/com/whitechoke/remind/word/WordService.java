package com.whitechoke.remind.word;

import com.whitechoke.remind.image.ImageEntity;
import com.whitechoke.remind.image.ImageRepository;
import com.whitechoke.remind.language.LanguageRepository;
import com.whitechoke.remind.pronunciation.localPronunciation.LocalPronunciationEntity;
import com.whitechoke.remind.pronunciation.localPronunciation.LocalPronunciationRepository;
import com.whitechoke.remind.storage.FileType;
import com.whitechoke.remind.storage.Storable;
import com.whitechoke.remind.storage.StorageFile;
import com.whitechoke.remind.storage.StorageService;
import com.whitechoke.remind.storage.fileOutbox.event.FileParentCreationEvent;
import com.whitechoke.remind.user.CurrentUserProvider;
import com.whitechoke.remind.user.UserRepository;
import com.whitechoke.remind.word.dto.WordCreateRequestDto;
import com.whitechoke.remind.word.dto.WordCreateResponseDto;
import com.whitechoke.remind.word.userWord.UserWordEntity;
import com.whitechoke.remind.word.userWord.UserWordRepository;
import com.whitechoke.remind.word.word.WordEntity;
import com.whitechoke.remind.word.word.WordRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

@Slf4j
@Service
@AllArgsConstructor
public class WordService {
    private final UserWordRepository userWordRepository;
    private final WordRepository wordRepository;
    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final LocalPronunciationRepository localPronunciationRepository;
    private final ImageRepository imageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final LanguageRepository languageRepository;

    @Transactional
    public WordCreateResponseDto createWord(
            WordCreateRequestDto request,
            MultipartFile wordImage,
            MultipartFile localPronunciation
    ) {
        var normalizedWord = request.word().trim().toLowerCase();
        var normalizedTranslation = request.transcription().trim().toLowerCase();

        var user = userRepository.findById(currentUserProvider.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id=" + currentUserProvider.getId()));

        var language = languageRepository.findByLanguageCode(request.languageCode())
                .orElseThrow(() ->  new EntityNotFoundException("Not found language with code=" + request.languageCode()));

        var wordEntity = wordRepository.findByWord(normalizedWord)
                .orElse(null);

        if (wordEntity == null) {

            var wordToCreate = WordEntity.builder()
                    .word(normalizedWord)
                    .translation(request.translation())
                    .language(language)
                    .build();

            //TODO: generate pronunciation async

            wordEntity = wordRepository.save(wordToCreate);
        }

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<StorageFile> localPronunciationPathFuture = localPronunciation != null
                    ? executor.submit(() -> storageService.save(
                            localPronunciation,
                            FileType.AUDIO)
                    )
                    : null;

            Future<StorageFile> imagePathFuture = wordImage != null
                    ? executor.submit(() -> storageService.save(
                            wordImage,
                            FileType.IMAGE)
                    )
                    : null;

            LocalPronunciationEntity localPronunciationEntity = saveToStorage(
                    localPronunciationPathFuture,
                    localPronunciationRepository,
                    LocalPronunciationEntity::new
            );

            ImageEntity imageEntity = saveToStorage(
                    imagePathFuture,
                    imageRepository,
                    ImageEntity::new
            );

            // TODO: add language id
            var createdWord = userWordRepository.save(
                    UserWordEntity.builder()
                            .user(user)
                            .word(wordEntity)
                            .sentence(request.sentence())
                            .image(imageEntity)
                            .transcription(normalizedTranslation)
                            .localPronunciation(localPronunciationEntity)
                            .build()
            );

            return WordCreateResponseDto.builder()
                    .id(createdWord.getId())
                    .word(createdWord.getWord().getWord())
                    .translation(createdWord.getWord().getTranslation())
                    .transcription(createdWord.getTranscription())
                    .sentence(createdWord.getSentence())
                    .build();

        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Failed to save files", e.getCause());
        }
    }

    private <T extends Storable> T saveToStorage(
            Future<StorageFile> future,
            JpaRepository<T, ?> repository,
            Supplier<T> factory
    ) throws ExecutionException, InterruptedException {
        if (future == null) return null;

        var result = future.get();
        var entity = storageService.saveToDb(
                result.path(),
                repository,
                factory
        );
        eventPublisher.publishEvent(new FileParentCreationEvent(result.id()));

        return entity;
    }
}
