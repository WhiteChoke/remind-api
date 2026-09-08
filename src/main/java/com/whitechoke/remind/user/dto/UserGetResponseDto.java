package com.whitechoke.remind.user.dto;

import lombok.Builder;

@Builder
public record UserGetResponseDto(
        Long id,
        String username,
        String email
) {
}
