package com.gurakbu.delivery.domain.admin.dto.request;

import lombok.Getter;

@Getter
public class AdminRequestDto {

    private String email;
    private String password;
    private String name;
    private String phone;
    private String role;
}
