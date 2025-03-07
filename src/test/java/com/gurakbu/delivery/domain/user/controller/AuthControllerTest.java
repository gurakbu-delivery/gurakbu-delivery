package com.gurakbu.delivery.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gurakbu.delivery.config.jwt.JwtTokenProvider;
import com.gurakbu.delivery.config.jwt.dto.TokenResponseDto;
import com.gurakbu.delivery.domain.user.dto.request.LoginRequestDto;
import com.gurakbu.delivery.domain.user.dto.request.SignUpRequestDto;
import com.gurakbu.delivery.domain.user.enums.UserRole;
import com.gurakbu.delivery.domain.user.repository.UserRepository;
import com.gurakbu.delivery.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.token.TokenService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private TokenResponseDto tokenResponseDto;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    



    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void 회원가입_성공() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "test@gmail.com",
                "Password123##",
                "010-1234-5678",
                "김배민",
                UserRole.USER
        );

        TokenResponseDto tokenResponseDto = new TokenResponseDto("mockAccessToken", "mockRefreshToken");

        when(userService.signUp(any(SignUpRequestDto.class))).thenReturn(tokenResponseDto);

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("mockRefreshToken"));
    }

    @Test
    void 회원가입_실패_유효하지_않은_이메일() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "test-gmail-com",   // 잘못된 이메일 형싱
                "Password123##",
                "010-1234-5678",
                "김배민",
                UserRole.USER
        );

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원가입_실패_잘못된_비밀번호() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "test@gmail.com",
                "0000", // 비밀번호 규칙 미준수
                "010-1234-5678",
                "김배민",
                UserRole.USER
        );

        // when & when
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        TokenResponseDto tokenResponseDto = new TokenResponseDto("mockAccessToken", "mockRefreshToken");

        when(userService.login(any(LoginRequestDto.class))).thenReturn(tokenResponseDto);

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("mockRefreshToken"));
    }

}