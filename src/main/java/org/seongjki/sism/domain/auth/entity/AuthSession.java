package org.seongjki.sism.domain.auth.entity;

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
public class AuthSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private LocalDateTime lastAccessedAt;

    private String ipAddress;

    private String userAgent;

    private String authKey;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public boolean isExpired(Clock clock) {
        return expiredAt.isBefore(LocalDateTime.now(clock));
    }

}
