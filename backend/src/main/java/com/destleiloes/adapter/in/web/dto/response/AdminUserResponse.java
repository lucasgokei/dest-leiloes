package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.application.port.in.admin.AdminUserView;
import com.destleiloes.domain.model.Role;
import java.time.Instant;

public record AdminUserResponse(
        String id,
        String name,
        String email,
        Role role,
        Instant createdAt,
        long auctionCount,
        long bidCount) {
    public static AdminUserResponse from(AdminUserView view) {
        return new AdminUserResponse(
                view.id(),
                view.name(),
                view.email(),
                view.role(),
                view.createdAt(),
                view.auctionCount(),
                view.bidCount());
    }
}
