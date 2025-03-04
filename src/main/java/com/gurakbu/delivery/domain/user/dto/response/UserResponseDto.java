package com.gurakbu.delivery.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final String phone;
    private final String role;

    public UserResponseDto(Long id, String email, String name, String phone, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }
}
