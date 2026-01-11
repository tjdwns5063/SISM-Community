package org.seongjki.sism.domain.post.dto;

import java.time.LocalDateTime;

public record PostDto(
        long id,
        String title,
        String author,
        LocalDateTime createdAt
) {
}
