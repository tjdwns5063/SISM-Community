package org.seongjki.sism.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.seongjki.sism.common.HttpException;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.auth.entity.AuthSession;
import org.seongjki.sism.domain.auth.persist.AuthSessionRepository;
import org.seongjki.sism.domain.user.persist.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Clock clock;

    private final AuthSessionRepository authSessionRepository;

    private final UserRepository userRepository;

    @Transactional
    public Optional<UserDetailDto> authenticate(String sessionKey) {
        AuthSession session = authSessionRepository.findByAuthKey(sessionKey)
                .orElseThrow(() -> new HttpException(HttpStatusCode.valueOf(404), "해당 세션키가 존재하지 않습니다."));

        if (session.isExpired(clock)) {
            return Optional.empty();
        }

        return Optional.of(UserDetailDto.builder()
                .id(session.getUser().getId())
                .email(session.getUser().getEmail())
                .password(session.getUser().getPassword())
                .role(session.getUser().getRole())
                .build());
    }

}
