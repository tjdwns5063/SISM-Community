package org.seongjki.sism.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.seongjki.sism.common.HttpException;
import org.seongjki.sism.domain.user.UserRole;
import org.seongjki.sism.domain.user.dto.SignUpRequest;
import org.seongjki.sism.domain.user.dto.UserDto;
import org.seongjki.sism.domain.user.entity.User;
import org.seongjki.sism.domain.user.persist.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Clock clock;

    @Transactional
    public UserDto signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new HttpException(HttpStatusCode.valueOf(409), "이미 존재하는 이메일입니다.");
        }

        User user = userRepository.save(User.builder()
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .nickname(request.nickname())
                .role(UserRole.ROLE_USER)
                .createdAt(LocalDateTime.now(clock))
                .updatedAt(LocalDateTime.now(clock))
                .build());

        return new UserDto(user.getId(), user.getNickname(), user.getEmail(), user.getName(), user.getPhoneNumber(), user.getCreatedAt());
    }

}
