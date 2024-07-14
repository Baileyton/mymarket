package com.core.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserDto {
    private String name;
    private String phone;
    private String address;

    @Builder
    public UpdateUserDto(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

}