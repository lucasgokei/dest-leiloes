package com.destleiloes.application.service;

import com.destleiloes.application.port.in.auth.GetUserProfileUseCase;
import com.destleiloes.application.port.in.auth.LoginCommand;
import com.destleiloes.application.port.in.auth.LoginUseCase;
import com.destleiloes.application.port.in.auth.SignupCommand;
import com.destleiloes.application.port.in.auth.SignupUseCase;
import com.destleiloes.application.port.out.PasswordHasherPort;
import com.destleiloes.application.port.out.UserRepositoryPort;
import com.destleiloes.domain.exception.ConflictException;
import com.destleiloes.domain.exception.UnauthorizedException;
import com.destleiloes.domain.model.Role;
import com.destleiloes.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements SignupUseCase, LoginUseCase, GetUserProfileUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;

    public AuthService(UserRepositoryPort userRepository, PasswordHasherPort passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    @Transactional
    public User signup(SignupCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ConflictException("Este e-mail já está cadastrado.");
        }

        User user = new User();
        user.setName(command.name());
        user.setEmail(command.email());
        user.setPasswordHash(passwordHasher.encode(command.password()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User login(LoginCommand command) {
        User user =
                userRepository
                        .findByEmail(command.email())
                        .orElseThrow(() -> new UnauthorizedException("E-mail ou senha inválidos."));

        if (!passwordHasher.matches(command.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("E-mail ou senha inválidos.");
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("Não autenticado."));
    }
}
