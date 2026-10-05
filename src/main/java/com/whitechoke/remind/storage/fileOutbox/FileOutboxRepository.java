package com.whitechoke.remind.storage.fileOutbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileOutboxRepository extends JpaRepository<FileOutboxEntity, Long> {
    Optional<FileOutboxEntity> findByHasLink(Long fileId);
}
