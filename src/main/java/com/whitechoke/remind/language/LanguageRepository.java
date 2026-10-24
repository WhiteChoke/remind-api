package com.whitechoke.remind.language;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
    @Query("select l from LanguageEntity l where l.languageCode = :code")
    @EntityGraph(value = "language_with_words", type = EntityGraph.EntityGraphType.LOAD)
    Optional<LanguageEntity> findLanguageWithWords(@Param("code") String code);
    Optional<LanguageEntity> findByLanguageCode(String code);
}
