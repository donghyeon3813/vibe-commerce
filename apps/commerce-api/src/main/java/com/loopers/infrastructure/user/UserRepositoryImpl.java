package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserModel save(UserModel user) {
        return userJpaRepository.save(user);
    }

    @Override
    public boolean existsByUserId(String id) {
        return userJpaRepository.existsByUserId(id);
    }

    @Override
    public UserModel findByUserId(String userId) {
        return userJpaRepository.findByUserId(userId);
    }
}
