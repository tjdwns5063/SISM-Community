package org.seongjki.sism.domain.post.dto;

import org.hibernate.validator.constraints.Length;

public record UpdatePostRequest(

        @Length(min = 1, max = 100)
        String title,

        @Length(max = 10000)
        String content
) {
}
