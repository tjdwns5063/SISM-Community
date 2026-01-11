package org.seongjki.sism.domain.post.dto;

import java.time.LocalDateTime;

public record DeletePostResponse(

        long id,

        LocalDateTime deletedAt
) {
}
