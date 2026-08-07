package com.destleiloes.adapter.in.web.security;

import com.destleiloes.domain.model.Role;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class SessionCookieService {

    private final JwtService jwtService;
    private final boolean cookieSecure;

    public SessionCookieService(
            JwtService jwtService, @Value("${app.cookie-secure}") boolean cookieSecure) {
        this.jwtService = jwtService;
        this.cookieSecure = cookieSecure;
    }

    public void issue(HttpServletResponse response, String userId, Role role) {
        String token = jwtService.generateToken(userId, role);
        ResponseCookie cookie =
                ResponseCookie.from(JwtCookieAuthenticationFilter.SESSION_COOKIE_NAME, token)
                        .httpOnly(true)
                        .secure(cookieSecure)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(jwtService.sessionDurationMs() / 1000)
                        .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void clear(HttpServletResponse response) {
        ResponseCookie cookie =
                ResponseCookie.from(JwtCookieAuthenticationFilter.SESSION_COOKIE_NAME, "")
                        .httpOnly(true)
                        .secure(cookieSecure)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(0)
                        .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
