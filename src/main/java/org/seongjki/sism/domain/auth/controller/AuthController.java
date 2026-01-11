package org.seongjki.sism.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.seongjki.sism.common.dto.ApiResponse;
import org.seongjki.sism.domain.auth.dto.SignInRequest;
import org.seongjki.sism.domain.auth.dto.SignInResponse;
import org.seongjki.sism.domain.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ApiResponse<SignInResponse> signIn(@RequestBody SignInRequest request,
                                              HttpServletRequest httpRequest,
                                              HttpServletResponse httpResponse) {

        SignInResponse res = authService.signIn(request, httpRequest.getRemoteAddr(),
                httpRequest.getHeader(HttpHeaders.USER_AGENT));

        addAuthCookie(httpResponse, res.sessionKey());

        return ApiResponse.success(res, "로그인에 성공했습니다.");
    }

    private void addAuthCookie(HttpServletResponse response, String sessionKey) {
        Cookie sessionCookie = new Cookie("SASEUM_SESSION", sessionKey);

        // 3. 쿠키 보안 설정
        sessionCookie.setHttpOnly(true);   // JS 접근 차단 (보안 필수)
        sessionCookie.setPath("/");        // 서비스 전역에서 쿠키 전송
        sessionCookie.setMaxAge(3600);     // 1시간 유효 (초 단위)
        // sessionCookie.setSecure(true);  // HTTPS 적용 시 주석 해제

        // 4. 응답 헤더에 쿠키 추가
        response.addCookie(sessionCookie);
    }

}
