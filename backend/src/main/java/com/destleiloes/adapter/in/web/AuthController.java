package com.destleiloes.adapter.in.web;

import com.destleiloes.adapter.in.web.dto.request.LoginRequest;
import com.destleiloes.adapter.in.web.dto.request.SignupRequest;
import com.destleiloes.adapter.in.web.dto.response.UserResponse;
import com.destleiloes.adapter.in.web.security.SessionCookieService;
import com.destleiloes.application.port.in.auth.LoginCommand;
import com.destleiloes.application.port.in.auth.LoginUseCase;
import com.destleiloes.application.port.in.auth.SignupCommand;
import com.destleiloes.application.port.in.auth.SignupUseCase;
import com.destleiloes.domain.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SignupUseCase signupUseCase;
    private final LoginUseCase loginUseCase;
    private final SessionCookieService sessionCookieService;

    public AuthController(
            SignupUseCase signupUseCase, LoginUseCase loginUseCase, SessionCookieService sessionCookieService) {
        this.signupUseCase = signupUseCase;
        this.loginUseCase = loginUseCase;
        this.sessionCookieService = sessionCookieService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody SignupRequest request, HttpServletResponse response) {
        User user = signupUseCase.signup(new SignupCommand(request.name(), request.email(), request.password()));
        sessionCookieService.issue(response, user.getId(), user.getRole());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        User user = loginUseCase.login(new LoginCommand(request.email(), request.password()));
        sessionCookieService.issue(response, user.getId(), user.getRole());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        sessionCookieService.clear(response);
        return ResponseEntity.noContent().build();
    }
}
