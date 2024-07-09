package com.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 조회 할 수 없습니다."),
    EXIST_USER(HttpStatus.BAD_REQUEST, "이미 가입 되어 있는 이메일입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
