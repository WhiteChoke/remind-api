package com.whitechoke.remind.pronunciation;

import com.whitechoke.remind.word.WordEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pronunciation")
public class PronunciationWordEntity extends PronunciationEntity {
    @OneToOne(mappedBy = "pronunciation")
    private WordEntity word;
}