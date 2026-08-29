package com.whitechoke.remind.user.dto;

import java.time.Instant;

public record CachedDto(
        Long id,
        String Username,
        String email,
        Instant lastReviewAt,
        String timezone
) {
}
