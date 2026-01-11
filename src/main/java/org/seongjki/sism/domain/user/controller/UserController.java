package org.seongjki.sism.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.seongjki.sism.common.dto.ApiResponse;
import org.seongjki.sism.domain.user.dto.SignUpRequest;
import org.seongjki.sism.domain.user.dto.UserDto;
import org.seongjki.sism.domain.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserDto> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return ApiResponse.success(userService.signUp(signUpRequest), "회원가입 성공");
    }

}
