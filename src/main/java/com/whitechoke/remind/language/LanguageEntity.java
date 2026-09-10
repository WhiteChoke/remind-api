package com.whitechoke.remind.language;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "languages")
public class LanguageEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "language_code", nullable = false, unique = true)
    private String languageCode;
}
