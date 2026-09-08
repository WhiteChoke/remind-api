package com.whitechoke.remind.security.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
