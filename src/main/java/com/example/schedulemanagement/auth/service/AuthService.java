package com.example.schedulemanagement.auth.service;

import com.example.schedulemanagement.auth.dto.request.LoginRequestDto;
import com.example.schedulemanagement.common.config.PasswordEncoder;
import com.example.schedulemanagement.common.exception.BaseException;
import com.example.schedulemanagement.common.exception.ErrorCode;
import com.example.schedulemanagement.user.entity.User;
import com.example.schedulemanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Long handleLogin(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new BaseException(ErrorCode.EMAIL_NOT_EXIST, null)
        );

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVAILD_PASSWORD, null);
        }

        return user.getId();
    }


}
