package com.whitechoke.remind.word.dto;

import jakarta.validation.constraints.NotBlank;

public record WordCreateRequestDto(
        @NotBlank
        String word,
        @NotBlank
        String translation,
        String transcription,
        String sentence,
        @NotBlank
        String languageCode
) {
}
