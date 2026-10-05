package com.whitechoke.remind.storage.fileOutbox;

import com.whitechoke.remind.storage.StorageService;
import com.whitechoke.remind.storage.fileOutbox.event.FileParentCreationEvent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@AllArgsConstructor
public class FileManager {
    private final FileOutboxRepository fileOutboxRepository;

    @Transactional
    @TransactionalEventListener
    public void changeLinkStateAfterCommit(
            FileParentCreationEvent event
    ) {
        var entity = fileOutboxRepository.findById(event.fileId())
                .orElseThrow(() -> new EntityNotFoundException("Not found file entity"));

        entity.setHasLink(true);
        log.info("Successfully changed file link state");
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void changeLinkStateAfterRollback(
            FileParentCreationEvent event
    ) {

        var entity = fileOutboxRepository.findById(event.fileId())
                .orElseThrow(() -> new EntityNotFoundException("Not found file entity"));

        fileOutboxRepository.delete(entity);
        log.info("File deleted successfully");
    }

}
