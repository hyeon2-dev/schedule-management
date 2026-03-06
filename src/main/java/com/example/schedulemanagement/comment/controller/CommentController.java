package com.example.schedulemanagement.comment.controller;

import com.example.schedulemanagement.comment.dto.request.CommentSaveRequestDto;
import com.example.schedulemanagement.comment.dto.request.CommentUpdateRequestDto;
import com.example.schedulemanagement.comment.dto.response.CommentResponseDto;
import com.example.schedulemanagement.comment.service.CommentService;
import com.example.schedulemanagement.common.Consts.Const;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommentResponseDto> saveComment(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentSaveRequestDto dto
    ) {
        return ResponseEntity.ok(commentService.saveComment(userId,scheduleId, dto));
    }

    @GetMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllComments (@PathVariable Long scheduleId) {
        return ResponseEntity.ok(commentService.getAllComments(scheduleId));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment (@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(commentService.updateComment(userId, commentId, dto));
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(userId, commentId);
    }

}
