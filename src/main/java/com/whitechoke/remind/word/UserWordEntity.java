package com.whitechoke.remind.word;

import com.whitechoke.remind.pronunciation.LocalPronunciationEntity;
import com.whitechoke.remind.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_words")
public class UserWordEntity {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @OneToOne
    @JoinColumn(name = "word_id")
    private WordEntity word;
    @Column(name = "translation", nullable = false)
    private String translation;
    @Column(name = "sentence")
    private String sentence;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "ease_factor")
    private Float easeFactor;
    @Column(name = "interval")
    private Integer interval;
    @Column(name = "repetitions")
    private Integer repetitions;
    @Column(name = "next_review_date")
    private Instant nextReviewDate;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private WordState state;
    @OneToOne
    @JoinColumn(name = "local_pronunciation_id")
    private LocalPronunciationEntity localPronunciation;
}
