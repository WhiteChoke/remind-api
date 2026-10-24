package com.whitechoke.remind.word.word;

import com.whitechoke.remind.language.LanguageEntity;
import com.whitechoke.remind.pronunciation.pronunciationWord.PronunciationWordEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "words",
        indexes = {
                @Index(name = "idx_words_word", columnList = "word", unique = true)
        }
)
public class WordEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "word", nullable = false)
    private String word;
    @Column(name = "translation", nullable = false)
    private String translation;
    @OneToOne
    @JoinColumn(name = "pronunciation_id")
    private PronunciationWordEntity pronunciation;
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private LanguageEntity language;
}
