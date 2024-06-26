package com.mymarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;
}
