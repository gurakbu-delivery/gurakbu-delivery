package com.gurakbu.delivery.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gurakbu.delivery.config.jwt.JwtTokenProvider;
import com.gurakbu.delivery.config.jwt.dto.TokenResponseDto;
import com.gurakbu.delivery.domain.user.dto.request.LoginRequestDto;
import com.gurakbu.delivery.domain.user.dto.request.SignUpRequestDto;
import com.gurakbu.delivery.domain.user.dto.request.UserRequestDto;
import com.gurakbu.delivery.domain.user.dto.response.UserResponseDto;
import com.gurakbu.delivery.domain.user.enums.UserRole;
import com.gurakbu.delivery.domain.user.repository.UserRepository;
import com.gurakbu.delivery.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.token.TokenService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

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

        when(userService.signUp(argThat(dto -> dto.getEmail().equals("test@gmail.com")))).thenReturn(tokenResponseDto);

        // when & then
        mockMvc.perform(post("/auth/signup")
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
        mockMvc.perform(post("/auth/signup")
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
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("test@gmail.com", "Password123##");
        TokenResponseDto tokenResponseDto = new TokenResponseDto("mockAccessToken", "mockRefreshToken");

        when(userService.login(argThat(dto -> dto.getEmail().equals("test@gmail.com")))).thenReturn(tokenResponseDto);

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("mockRefreshToken"));
    }

    @Test
    void 회원정보_수정_성공() throws Exception {
        // given
        UserRequestDto requestDto = new UserRequestDto(null, "newPassword123##", "김쿠팡이츠", "010-5678-1234", null);
        UserResponseDto responseDto = new UserResponseDto(null, "newPassword123##", "김쿠팡이츠", "010-5678-1234", null);
        TokenResponseDto tokenResponseDto = new TokenResponseDto("mockAccessToken", "mockRefreshToken");

        when(userService.updateUser(any(String.class), any(UserRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(put("/auth/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("mockRefreshToken"))
                .andExpect(jsonPath("$.password").value("newPassword123##"))
                .andExpect(jsonPath("$.name").value("김쿠팡이츠"))
                .andExpect(jsonPath("$.phoneNumber").value("010-5678-1234"));
    }

    @Test
    void 회원정보_수정_실패_토큰없음() throws Exception {
        // given
        UserRequestDto requestDto = new UserRequestDto(null, "newPassword123##", "김쿠팡이츠", "010-5678-1234", null);

        // when & then
        mockMvc.perform(put("/auth/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원탈퇴_성공() throws Exception {
        // given
        String username = "testUser";
        String password = "Password123##";

        Mockito.doNothing().when(userService).deleteUser(username, password);

        // when & then
        mockMvc.perform(delete("/auth/delete")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("회원탈퇴가 완료되었습니다."));
    }

    @Test
    void Access_Token_재발급_성공() throws Exception {
        // given
        String refreshToken = "mockRefreshToken";
        TokenResponseDto tokenResponseDto = new TokenResponseDto("newAccessToken", "newRefreshToken");

        when(userService.reissueAccessToken(refreshToken)).thenReturn(tokenResponseDto);

        // when & then
        mockMvc.perform(post("/auth/reissue")
                        .param("refreshToken", refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("newRefreshToken"));

    }
}