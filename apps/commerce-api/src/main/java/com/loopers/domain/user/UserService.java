package com.loopers.domain.user;

import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserModel createUser(UserV1Dto.SignupRequest signupRequest) {
        // 이미 존재하는 회원인지 확인
        boolean isExists = userRepository.existsByUserId(signupRequest.getId());
        if (isExists) {
            throw new CoreException(ErrorType.CONFLICT, ErrorType.CONFLICT.getMessage());
        }
        // 회원 생성
        UserModel userModel = UserModel.CreateUser(signupRequest.getId(),
                signupRequest.getEmail(),
                signupRequest.getGender(),
                signupRequest.getBirthday());
        return userRepository.save(userModel);
    }

    public UserModel getUser(String userId) {

        return userRepository.findByUserId(userId);
    }
}
