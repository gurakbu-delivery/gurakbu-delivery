package com.gurakbu.delivery.domain.user.dto.request;

import com.gurakbu.delivery.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private UserRole userRole;
}
