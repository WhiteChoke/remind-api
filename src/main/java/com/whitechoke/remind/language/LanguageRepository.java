package com.whitechoke.remind.language;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
    @EntityGraph(value = "language_with_words", type = EntityGraph.EntityGraphType.LOAD)
    Optional<LanguageEntity> findLanguageWithWords(String code);
}
