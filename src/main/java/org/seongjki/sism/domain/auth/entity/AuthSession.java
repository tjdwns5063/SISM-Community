package org.seongjki.sism.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.seongjki.sism.domain.user.entity.User;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

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

    public void renew(String ipAddress, String userAgent, Clock clock) {
        this.authKey = UUID.randomUUID().toString(); // 키 교체 (Session Rotation)
        this.expiredAt = LocalDateTime.now(clock).plusHours(1); // 만료시간 갱신
        this.ipAddress = ipAddress; // IP 갱신 (장소 바뀜 등)
        this.userAgent = userAgent; // 기기 정보 갱신
    }

}
