package com.destleiloes.adapter.out.persistence.mapper;

import com.destleiloes.adapter.out.persistence.entity.UserJpaEntity;
import com.destleiloes.domain.model.User;

public final class UserEntityMapper {

    private UserEntityMapper() {}

    public static User toDomain(UserJpaEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPasswordHash(entity.getPasswordHash());
        user.setRole(entity.getRole());
        user.setCreatedAt(entity.getCreatedAt());
        return user;
    }

    public static UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRole(user.getRole());
        entity.setCreatedAt(user.getCreatedAt());
        return entity;
    }
}
