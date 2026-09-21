package com.whitechoke.remind.pronunciation;

import com.whitechoke.remind.storage.Storable;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class PronunciationEntity implements Storable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "pronunciation", nullable = false)
    private String path;

    @Override
    public void setPath(String path) {
        this.path = path;
    }
}
