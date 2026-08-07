package com.destleiloes.application.port.in.admin;

import com.destleiloes.domain.model.Role;
import java.time.Instant;

public record AdminUserView(
        String id, String name, String email, Role role, Instant createdAt, long auctionCount, long bidCount) {}
