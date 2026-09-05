package com.whitechoke.remind.security.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String timezone,
        String email
) {
}
