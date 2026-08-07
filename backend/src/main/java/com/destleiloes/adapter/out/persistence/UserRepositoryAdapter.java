package com.destleiloes.adapter.out.persistence;

import com.destleiloes.adapter.out.persistence.mapper.UserEntityMapper;
import com.destleiloes.adapter.out.persistence.repository.SpringDataUserRepository;
import com.destleiloes.application.port.out.UserRepositoryPort;
import com.destleiloes.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository userJpaRepository;

    public UserRepositoryAdapter(SpringDataUserRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return UserEntityMapper.toDomain(userJpaRepository.save(UserEntityMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(String id) {
        return userJpaRepository.findById(id).map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntityMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAllByOrderByCreatedAtDesc() {
        return userJpaRepository.findAllByOrderByCreatedAtDesc().stream().map(UserEntityMapper::toDomain).toList();
    }

    @Override
    public List<User> findAllById(List<String> ids) {
        return userJpaRepository.findAllById(ids).stream().map(UserEntityMapper::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        userJpaRepository.deleteById(id);
    }
}
