package com.whitechoke.remind.word.word;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<WordEntity, Long> {
    Optional<WordEntity> findByWord(String word);
}
