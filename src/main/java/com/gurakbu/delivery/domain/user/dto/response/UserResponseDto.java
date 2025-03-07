package com.gurakbu.delivery.domain.user.dto.response;

import com.gurakbu.delivery.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final String phoneNumber;
    private final UserRole role;

    public UserResponseDto(Long id, String email, String name, String phoneNumber, UserRole role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
