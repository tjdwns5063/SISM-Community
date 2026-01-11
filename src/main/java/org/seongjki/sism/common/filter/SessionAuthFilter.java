package org.seongjki.sism.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.seongjki.sism.domain.auth.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class SessionAuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                .filter(c -> "SASEUM_SESSION".equals(c.getName())).findFirst();

        if (cookie.isPresent()) {
            String sessionKey = cookie.get().getValue();

            authService.authenticate(sessionKey).ifPresent(userDetail -> SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities())));
        }

        filterChain.doFilter(request, response);
    }

}
