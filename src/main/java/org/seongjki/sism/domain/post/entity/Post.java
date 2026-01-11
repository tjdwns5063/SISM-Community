package org.seongjki.sism.domain.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.seongjki.sism.domain.user.entity.User;

import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Integer viewCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void update(String title, String content, Clock clock) {
        if (!title.isBlank()) {
            this.title = title;
        }
        if (!content.isBlank()) {
            this.content = content;
        }
        updatedAt = LocalDateTime.now(clock);
    }

    public void delete(Clock clock) {
        deletedAt = LocalDateTime.now(clock);
    }

}
