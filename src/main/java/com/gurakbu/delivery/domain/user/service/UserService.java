package com.gurakbu.delivery.domain.user.service;

import com.gurakbu.delivery.config.PasswordEncoder;
import com.gurakbu.delivery.config.jwt.JwtTokenProvider;
import com.gurakbu.delivery.config.jwt.dto.TokenResponseDto;
import com.gurakbu.delivery.config.jwt.entity.RefreshToken;
import com.gurakbu.delivery.config.jwt.service.RefreshTokenService;
import com.gurakbu.delivery.domain.user.dto.request.LoginRequestDto;
import com.gurakbu.delivery.domain.user.dto.request.SignUpRequestDto;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.enums.UserRole;
import com.gurakbu.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;


    @Transactional
    public TokenResponseDto signUp(SignUpRequestDto requestDto){

        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new IllegalStateException("이미 가입된 사용자 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        UserRole userRole = requestDto.getUserRole() != null ? requestDto.getUserRole() : UserRole.USER;

        User user = new User(
                requestDto.getEmail(),
                encodedPassword,
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                userRole
        );

        userRepository.save(user);
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), user.getUserRole().name());

        // Refresh Token 생성 및 저장
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return new TokenResponseDto(accessToken, refreshToken.getRefreshToken());

    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto requestDto){
        User user = userRepository.findByEmailAndIsDeletedFalse(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이나 비밀번호가 일치하지 않습니다."));

        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
            throw new IllegalStateException("이메일이나 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(),user.getUserRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return new TokenResponseDto(accessToken, refreshToken.getRefreshToken());
    }

//    @Transactional
//    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
//        User user = userRepository.findById(id).orElseThrow(
//                () -> new IllegalStateException("해당 id의 회원이 존재하지 않습니다.")
//        );
//
//        String newPassword = userRequestDto.getPassword();
//        if (newPassword != null && !newPassword.isBlank()) {
//            newPassword = passwordEncoder.encode(newPassword);
//        } else {
//            newPassword = user.getPassword();
//        }
//
//        user.update(newPassword, userRequestDto.getName(), userRequestDto.getPhone());
//        return new UserResponseDto(user.getId(), user.getEmail(), user.getName(), user.getPhone(), user.getRole());
//    }


    @Transactional
    public void deleteUser(String userEmail, String rawPassword) {
        User user = userRepository.findByEmailAndIsDeletedFalse(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("이메일이나 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("이메일이나 비밀번호가 일치하지 않습니다.");
        }
        user.setDeleted(true);
        userRepository.save(user);
        refreshTokenService.deleteByEmail(userEmail);
    }

    // Access Token 재발급 API (리프레시 토큰 사용)
    @Transactional
    public TokenResponseDto reissueAccessToken(String refreshToken) {
        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }
        // DB에 저장된 토큰과 일치하는지 확인
        RefreshToken storedToken = refreshTokenService.verifyExpiration(
                refreshTokenService.getByToken(refreshToken)
                        .orElseThrow(() -> new IllegalArgumentException("Refresh Token을 찾을 수 없습니다."))
        );


        String username = storedToken.getEmail();
        User user = userRepository.findByEmailAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), user.getUserRole().name());
        // 필요 시 새로운 Refresh Token 발급 및 DB 갱신 가능 (여기서는 그대로 사용)
        return new TokenResponseDto(newAccessToken, storedToken.getRefreshToken());
    }
}
