package com.gurakbu.delivery.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;

}
