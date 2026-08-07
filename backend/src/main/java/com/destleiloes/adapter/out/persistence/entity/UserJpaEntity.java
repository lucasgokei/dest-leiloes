package com.destleiloes.adapter.out.persistence.entity;

import com.destleiloes.domain.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "User")
@Getter
@Setter
@NoArgsConstructor
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "TEXT")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "passwordHash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role", columnDefinition = "\"Role\"", nullable = false)
    private Role role = Role.USER;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
