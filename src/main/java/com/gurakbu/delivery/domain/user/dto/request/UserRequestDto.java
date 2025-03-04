package com.gurakbu.delivery.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserRequestDto {

    private String email;
    private String password;
    private String name;
    private String phone;
    private String role;
}
