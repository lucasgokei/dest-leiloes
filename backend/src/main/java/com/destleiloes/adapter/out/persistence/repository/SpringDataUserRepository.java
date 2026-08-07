package com.destleiloes.adapter.out.persistence.repository;

import com.destleiloes.adapter.out.persistence.entity.UserJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, String> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UserJpaEntity> findAllByOrderByCreatedAtDesc();
}
