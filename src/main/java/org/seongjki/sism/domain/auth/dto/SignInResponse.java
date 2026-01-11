package org.seongjki.sism.domain.auth.dto;

import org.seongjki.sism.domain.user.dto.UserDto;

public record SignInResponse(
        UserDto user,
        String sessionKey
) {
}
