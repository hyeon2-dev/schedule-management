package com.example.schedulemanagement.user.service;

import com.example.schedulemanagement.common.config.PasswordEncoder;
import com.example.schedulemanagement.common.exception.BaseException;
import com.example.schedulemanagement.common.exception.ErrorCode;
import com.example.schedulemanagement.user.dto.request.UserSaveRequestDto;
import com.example.schedulemanagement.user.dto.request.UserUpdateRequestDto;
import com.example.schedulemanagement.user.dto.response.UserResponseDto;
import com.example.schedulemanagement.user.entity.User;
import com.example.schedulemanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto saveUser(UserSaveRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BaseException(ErrorCode.EAMIL_ALREDAY_EXIST, null);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User(dto.getUserName(), dto.getEmail(), encodedPassword);
        userRepository.save(user);

        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> dtoList = new ArrayList<>();

        for(User user : users) {
            UserResponseDto userResponseDto = new UserResponseDto(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getCreatedAt(),
                    user.getModifiedAt()
            );
            dtoList.add(userResponseDto);
        }

        return dtoList;
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );


    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        user.update(dto.getUserName(), dto.getEmail(), encodedPassword);

        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        userRepository.delete(user);
    }
}
