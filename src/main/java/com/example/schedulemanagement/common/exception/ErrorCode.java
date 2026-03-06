package com.example.schedulemanagement.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 유저 관련 코드
    EMAIL_NOT_EXIST("해당 이메일이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("패스워드가 일치하지 않습니다", HttpStatus.UNAUTHORIZED),
    EMAIL_ALREADY_EXIST("이미 사용중인 이메일입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("id에 맞는 유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 스케줄 관련 코드
    SCHEDULE_NOT_FOUND("id에 맞는 스케줄이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    SCHEDULE_FORBIDDEN_ACCESS("본인이 작성한 스케줄만 수정 및 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),

    // 댓글 관련 코드
    COMMENT_NOT_FOUND("id에 맞는 댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    COMMENT_FORBIDDEN_ACCESS("본인이 작성한 댓글만 수정 및 삭제할 수 있습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;
}
