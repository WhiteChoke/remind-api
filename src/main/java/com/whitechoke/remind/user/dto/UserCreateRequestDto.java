package com.whitechoke.remind.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequestDto (
        @NotBlank(message = "Username must be not blank")
        String username,
        @NotBlank(message = "Password must not be blank")
        String password,
        String email,
        @NotBlank(message = "Timezone must not be blank")
        String timezone
) {
}
