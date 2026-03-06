package com.example.schedulemanagement.comment.service;

import com.example.schedulemanagement.comment.dto.request.CommentSaveRequestDto;
import com.example.schedulemanagement.comment.dto.request.CommentUpdateRequestDto;
import com.example.schedulemanagement.comment.dto.response.CommentResponseDto;
import com.example.schedulemanagement.comment.entity.Comment;
import com.example.schedulemanagement.comment.repository.CommentRepository;
import com.example.schedulemanagement.common.exception.BaseException;
import com.example.schedulemanagement.common.exception.ErrorCode;
import com.example.schedulemanagement.schedule.entity.Schedule;
import com.example.schedulemanagement.schedule.repository.ScheduleRepository;
import com.example.schedulemanagement.user.entity.User;
import com.example.schedulemanagement.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CommentResponseDto saveComment(Long userId, Long scheduleId, CommentSaveRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new BaseException(ErrorCode.SCHEDULE_NOT_FOUND, null)
        );

        Comment comment = new Comment(user, schedule, dto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getSchedule().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );

    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllComments(Long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new BaseException(ErrorCode.SCHEDULE_NOT_FOUND, null)
        );

        List<Comment> comments = commentRepository.findAll();
        List<CommentResponseDto> dtoList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(
                    comment.getId(),
                    comment.getUser().getId(),
                    comment.getSchedule().getId(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt()
            );
            dtoList.add(commentResponseDto);
        }
        return dtoList;
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new BaseException(ErrorCode.COMMENT_NOT_FOUND, null)
        );

        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getSchedule().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }

    @Transactional
    public CommentResponseDto updateComment(Long userId, Long commentId, CommentUpdateRequestDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new BaseException(ErrorCode.COMMENT_NOT_FOUND, null)
        );

        if(!comment.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.COMMENT_FORBIDDEN_ACCESS, null);
        }

        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getSchedule().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );

    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new BaseException(ErrorCode.COMMENT_NOT_FOUND, null)
        );

        if(!comment.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.COMMENT_FORBIDDEN_ACCESS, null);
        }

        commentRepository.deleteById(commentId);
    }
}
