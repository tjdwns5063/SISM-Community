package org.seongjki.sism.domain.post.dto;

import java.time.LocalDateTime;

public record UpdatePostResponse(

        long id,

        LocalDateTime updatedAt
) {
}
