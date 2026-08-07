package com.destleiloes.application.port.in.auth;

import com.destleiloes.domain.model.User;

public interface GetUserProfileUseCase {

    User getById(String userId);
}
