package org.seongjki.sism.domain.post.dto;

import java.time.LocalDateTime;

public record PostDetailDto(

        long id,

        String title,

        String content,

        String author,

        LocalDateTime createdAt
) {
}
