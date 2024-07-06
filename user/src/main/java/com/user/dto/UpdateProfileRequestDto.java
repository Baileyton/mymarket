package com.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProfileRequestDto {
    @NotBlank
    private String newAddress;
    @NotBlank
    private String newPhone;
}
