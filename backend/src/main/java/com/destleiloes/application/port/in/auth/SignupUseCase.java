package com.destleiloes.application.port.in.auth;

import com.destleiloes.domain.model.User;

public interface SignupUseCase {

    User signup(SignupCommand command);
}
