package com.destleiloes.application.port.out;

import com.destleiloes.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByOrderByCreatedAtDesc();

    List<User> findAllById(List<String> ids);

    void deleteById(String id);
}
