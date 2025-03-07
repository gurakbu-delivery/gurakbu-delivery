package com.gurakbu.delivery.domain.user.controller;

import com.gurakbu.delivery.config.jwt.dto.TokenResponseDto;
import com.gurakbu.delivery.domain.user.dto.request.LoginRequestDto;
import com.gurakbu.delivery.domain.user.dto.request.SignUpRequestDto;
import com.gurakbu.delivery.domain.user.dto.request.UserRequestDto;
import com.gurakbu.delivery.domain.user.dto.response.UserResponseDto;
import com.gurakbu.delivery.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<TokenResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestDto) {
        TokenResponseDto tokens = userService.signUp(requestDto);
        return ResponseEntity.ok(tokens);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        TokenResponseDto tokens = userService.login(requestDto);
        return ResponseEntity.ok(tokens);
    }

    // 회원정보수정
    @PutMapping("/users")
    public ResponseEntity<UserResponseDto> updateUser(HttpServletRequest request, @RequestBody UserRequestDto userRequestDto) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        token = token.substring(7);

        UserResponseDto updateUser = userService.updateUser(token, userRequestDto);
        return ResponseEntity.ok(updateUser);
    }

    // 회원탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(
            @RequestParam String username,
            @RequestParam String password
    ) {
        userService.deleteUser(username, password);
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }

    // Access Token 재발급 (Refresh Token 사용)
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissueToken(@RequestParam String refreshToken) {
        TokenResponseDto tokens = userService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(tokens);
    }
}
