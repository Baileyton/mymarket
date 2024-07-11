package com.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    USER("사용자"),
    ADMIN("관리자");

    private final String role;
}