package com.user.dto;

import com.core.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class RegisterDto {
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotBlank
    private String password;
    private String phone;
    private String address;

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .phone(phone)
                .address(address)
                .build();
    }

    @Builder
    public RegisterDto(String email, String password, String name, String phone, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
}