package com.destleiloes.adapter.in.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

    public static final String SESSION_COOKIE_NAME = "session";

    private final JwtService jwtService;

    public JwtCookieAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        readCookie(request, SESSION_COOKIE_NAME)
                .flatMap(jwtService::parseToken)
                .ifPresent(claims -> {
                    var principal = new AuthenticatedUser(claims.userId(), claims.role());
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + claims.role().name()));
                    var authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });

        filterChain.doFilter(request, response);
    }

    private java.util.Optional<String> readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return java.util.Optional.empty();
        }
        return java.util.Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
