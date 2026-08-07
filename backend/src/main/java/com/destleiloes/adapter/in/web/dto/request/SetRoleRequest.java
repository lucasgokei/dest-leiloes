package com.destleiloes.adapter.in.web.dto.request;

import com.destleiloes.domain.model.Role;
import jakarta.validation.constraints.NotNull;

public record SetRoleRequest(@NotNull Role role) {}
