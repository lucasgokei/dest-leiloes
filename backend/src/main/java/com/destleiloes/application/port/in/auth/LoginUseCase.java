package com.destleiloes.application.port.in.auth;

import com.destleiloes.domain.model.User;

public interface LoginUseCase {

    User login(LoginCommand command);
}
