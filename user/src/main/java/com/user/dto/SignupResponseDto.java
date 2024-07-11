package com.user.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private String name;
    private String email;
    private String message;

    public SignupResponseDto(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
    }
}
