package com.whitechoke.remind.storage.fileOutbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_outbox")
public class FileOutboxEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "file_path", nullable = false, unique = true)
    private String filePath;
    @Builder.Default
    @Column(name = "has_link")
    private boolean hasLink = false;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
