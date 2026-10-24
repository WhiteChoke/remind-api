package com.whitechoke.remind.word.dto;

import lombok.Builder;

@Builder
public record WordCreateResponseDto(
        Long id,
        String word,
        String translation,
        String transcription,
        String sentence
) {
}
