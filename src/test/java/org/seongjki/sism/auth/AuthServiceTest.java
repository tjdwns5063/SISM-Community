package org.seongjki.sism.auth;

import org.junit.jupiter.api.Test;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.auth.entity.AuthSession;
import org.seongjki.sism.domain.auth.persist.AuthSessionRepository;
import org.seongjki.sism.domain.auth.service.AuthService;
import org.seongjki.sism.domain.user.UserRole;
import org.seongjki.sism.domain.user.entity.User;
import org.seongjki.sism.domain.user.persist.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class AuthServiceTest {

    private AuthService authService;

    private UserRepository userRepository;

    private AuthSessionRepository authSessionRepository;

    private Clock clock;

    @Autowired
    public AuthServiceTest(AuthSessionRepository authSessionRepository, UserRepository userRepository) {
        LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 1, 12, 30);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        this.authSessionRepository = authSessionRepository;
        this.userRepository = userRepository;
        this.authService = new AuthService(clock, authSessionRepository, userRepository);
    }

    @Test
    void 인증_성공() {
        //given
        User user = userRepository.save(User.builder()
                        .name("test")
                        .email("test@abc.com")
                        .password("1234")
                        .createdAt(LocalDateTime.now(clock))
                        .updatedAt(LocalDateTime.now(clock))
                        .phoneNumber("010-1234-5678")
                        .role(UserRole.ROLE_USER)
                .build());

        AuthSession authSession = authSessionRepository.save(AuthSession.builder()
                        .authKey("test-key")
                        .createdAt(LocalDateTime.now(clock))
                        .user(user)
                        .expiredAt(LocalDateTime.now(clock).plusHours(1))
                .build());

        //when
        Optional<UserDetailDto> res = authService.authenticate("test-key");

        //then
        assertThat(res)
                .isNotEmpty()
                .get()
                .isEqualTo(UserDetailDto.builder()
                        .id(authSession.getId())
                        .email("test@abc.com")
                        .password("1234")
                        .role(UserRole.ROLE_USER)
                .build());
    }


}
