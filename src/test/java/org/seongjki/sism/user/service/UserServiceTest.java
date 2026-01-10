package org.seongjki.sism.user.service;

import org.junit.jupiter.api.Test;
import org.seongjki.sism.common.HttpException;
import org.seongjki.sism.domain.user.dto.SignUpRequest;
import org.seongjki.sism.domain.user.dto.UserDto;
import org.seongjki.sism.domain.user.entity.User;
import org.seongjki.sism.domain.user.persist.UserRepository;
import org.seongjki.sism.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
public class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;

    private Clock clock;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceTest(UserRepository userRepository) {
        LocalDateTime localDateTime = LocalDateTime.of(2026, 1, 1, 12, 30);
        this.userRepository = userRepository;
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        this.userService = new UserService(userRepository, passwordEncoder, clock);
    }

    @Test
    void 회원가입_테스트() {
        //given
        SignUpRequest request = new SignUpRequest(
                "n1",
                "n1",
                "abc@naver.com",
                "abcd1234q!",
                "01012345678"
        );

        //when
        UserDto res = userService.signUp(request);

        //then
        assertThat(res).isEqualTo(new UserDto(
                res.id(),
                "n1",
                "abc@naver.com",
                "n1",
                "01012345678",
                LocalDateTime.of(2026, 1, 1, 12, 30)
        ));
    }

    @Test
    void 회원가입_이메일_중복() {
        //given
        User existUser = userRepository.save(User.builder()
                .email("aaa@abc.com")
                .password(passwordEncoder.encode("123"))
                .build());

        //when,then
        assertThatThrownBy(() -> userService.signUp(
                new SignUpRequest("n1", "n1", "aaa@abc.com", "123", "01012345678"))
        ).isInstanceOf(HttpException.class)
                .hasMessage("409 이미 존재하는 이메일입니다.");
    }

}
