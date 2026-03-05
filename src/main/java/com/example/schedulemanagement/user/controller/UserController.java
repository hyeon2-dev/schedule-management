package com.example.schedulemanagement.user.controller;

import com.example.schedulemanagement.common.Consts.Const;
import com.example.schedulemanagement.user.dto.request.UserSaveRequestDto;
import com.example.schedulemanagement.user.dto.request.UserUpdateRequestDto;
import com.example.schedulemanagement.user.dto.response.UserResponseDto;
import com.example.schedulemanagement.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<UserResponseDto> saveUser(UserSaveRequestDto dto) {
        return ResponseEntity.ok(userService.saveUser(dto));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users/me")
    public ResponseEntity<UserResponseDto> updateUser(
            @SessionAttribute (name = Const.LOGIN_USER) Long userId,
            @RequestBody UserUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(userService.updateUser(userId, dto));
    }

    @DeleteMapping("/users/me")
    public void deleteUser(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            HttpServletRequest request
    ) {
        userService.deleteUser(userId);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
