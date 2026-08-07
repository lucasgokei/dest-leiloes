package com.destleiloes.application.port.in.admin;

public interface AdminDeleteUserUseCase {

    void deleteUser(String adminId, String targetUserId);
}
