package com.destleiloes.domain.model;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private String passwordHash;
    private Role role = Role.USER;
    private Instant createdAt = Instant.now();
}
