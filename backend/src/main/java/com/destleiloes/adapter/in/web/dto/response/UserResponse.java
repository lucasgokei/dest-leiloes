package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.domain.model.Role;
import com.destleiloes.domain.model.User;
import java.time.Instant;

public record UserResponse(String id, String name, String email, Role role, Instant createdAt) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }
}
