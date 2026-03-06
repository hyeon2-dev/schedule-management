package com.example.schedulemanagement.schedule.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SchedulePageResponseDto {

    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final int commentCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public SchedulePageResponseDto(Long id, Long userId, String title, String content, int commentCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
