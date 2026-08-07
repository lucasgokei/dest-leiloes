package com.destleiloes.application.port.in.admin;

import com.destleiloes.domain.model.Role;

public interface AdminSetUserRoleUseCase {

    void setRole(String adminId, String targetUserId, Role role);
}
