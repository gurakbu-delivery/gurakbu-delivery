package com.gurakbu.delivery.domain.admin.dto.response;

import lombok.Getter;

@Getter
public class AdminResponseDto {

    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;

    public AdminResponseDto(Long id, String email, String password, String name, String phone, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }
}
