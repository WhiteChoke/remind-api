package com.whitechoke.remind.security.domain.dto;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String username,
        Long id,
        String accessToken
) {
}
