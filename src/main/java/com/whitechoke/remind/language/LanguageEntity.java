package com.whitechoke.remind.language;

import com.whitechoke.remind.word.WordEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "languages")
@NamedEntityGraph(
        name = "language_with_words",
        attributeNodes = {
                @NamedAttributeNode("wordList")
        }
)
public class LanguageEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "language_code", nullable = false, unique = true)
    private String languageCode;
    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    private List<WordEntity> wordList;
}
