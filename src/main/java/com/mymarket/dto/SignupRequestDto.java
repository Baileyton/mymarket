package com.mymarket.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;
}