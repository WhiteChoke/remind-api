package com.whitechoke.remind.pronunciation.localPronunciation;

import com.whitechoke.remind.pronunciation.PronunciationEntity;
import com.whitechoke.remind.word.userWord.UserWordEntity;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "local_pronunciation")
public class LocalPronunciationEntity extends PronunciationEntity {
    @OneToOne(mappedBy = "localPronunciation")
    private UserWordEntity userWord;
}
