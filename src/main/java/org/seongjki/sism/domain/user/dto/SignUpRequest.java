package org.seongjki.sism.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        @NotBlank
        String nickname,

        @NotBlank
        String name,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+|<>?:\"{}])[A-Za-z\\d~!@#$%^&*()_+|<>?:\"{}]{10,}$")
        String password,

        @NotBlank
        String phoneNumber
) {
}
