package com.destleiloes.adapter.in.web.security;

import com.destleiloes.domain.model.Role;

public record AuthenticatedUser(String userId, Role role) {}
