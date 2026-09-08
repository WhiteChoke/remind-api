package com.whitechoke.remind.security.domain.dto;

import lombok.Builder;

@Builder
public record AccessTokenGetResponseDto(
        String token,
        Long id,
        String username
){
}
