package com.whitechoke.remind.pronunciation;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class PronunciationEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "pronunciation", nullable = false)
    private String pronunciation_path;
}
