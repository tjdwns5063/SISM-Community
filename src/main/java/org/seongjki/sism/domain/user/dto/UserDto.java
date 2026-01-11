package org.seongjki.sism.domain.user.dto;

import java.time.LocalDateTime;

public record UserDto(

        long id,

        String nickname,

        String email,

        String name,

        String phoneNumber,

        LocalDateTime createdAt
) {
}
