package com.whitechoke.remind.user.dto;

import lombok.Builder;

@Builder
public record UserCreateResponseDto (
        Long id,
        String username
) {
}
