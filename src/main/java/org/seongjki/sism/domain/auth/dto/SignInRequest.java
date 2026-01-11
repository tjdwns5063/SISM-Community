package org.seongjki.sism.domain.auth.dto;

public record SignInRequest(
        String email,
        String password
) {
}
