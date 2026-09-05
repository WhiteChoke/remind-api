package com.whitechoke.remind.security.domain.dto;

import lombok.Builder;

@Builder
public record RegisterResponseDto(
        String username,
        Long id,
        String accessToken
) {
}
