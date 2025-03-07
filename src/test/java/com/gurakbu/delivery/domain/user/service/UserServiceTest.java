package com.gurakbu.delivery.domain.user.service;

import com.gurakbu.delivery.config.PasswordEncoder;
import com.gurakbu.delivery.config.jwt.JwtTokenProvider;
import com.gurakbu.delivery.config.jwt.dto.TokenResponseDto;
import com.gurakbu.delivery.config.jwt.entity.RefreshToken;
import com.gurakbu.delivery.config.jwt.service.RefreshTokenService;
import com.gurakbu.delivery.domain.user.dto.request.LoginRequestDto;
import com.gurakbu.delivery.domain.user.dto.request.SignUpRequestDto;
import com.gurakbu.delivery.domain.user.dto.request.UserRequestDto;
import com.gurakbu.delivery.domain.user.dto.response.UserResponseDto;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.enums.UserRole;
import com.gurakbu.delivery.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private UserService userService;

    @Test
    void 회원_중복된_이메일_회원가입시_IllegalStateException를_던진다() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto("test@gmail.com", "Password123##", "김배민", "010-1234-5678", UserRole.USER);
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.signUp(requestDto));

        // then
        assertEquals("이미 가입된 사용자 이메일입니다.", exception.getMessage());
        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void 사장_중복된_이메일_회원가입시_IllegalStateException를_던진다() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto("test@gmail.com", "Password123##", "김사장", "010-1234-5678", UserRole.OWNER);
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.signUp(requestDto));

        // then
        assertEquals("이미 가입된 사용자 이메일입니다.", exception.getMessage());
        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void 회원_사용가능_이메일_회원가입() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto("test@gmail.com", "Password123##", "김배민", "010-1234-5678", UserRole.USER);
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(jwtTokenProvider.generateAccessToken(anyString(), anyString())).thenReturn("mockAccessToken");

        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setRefreshToken("mockRefreshToken");
        when(refreshTokenService.createRefreshToken(requestDto.getEmail())).thenReturn(mockRefreshToken);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // when
        TokenResponseDto responseDto = userService.signUp(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("mockAccessToken", responseDto.getAccessToken());
        assertEquals("mockRefreshToken", responseDto.getRefreshToken());
        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(passwordEncoder).encode(requestDto.getPassword());
        verify(userRepository).save(userCaptor.capture());
        verify(refreshTokenService).createRefreshToken(requestDto.getEmail());

        User savedUser = userCaptor.getValue();
        assertEquals(requestDto.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(requestDto.getName(), savedUser.getName());
        assertEquals(requestDto.getPhoneNumber(), savedUser.getPhoneNumber());
        assertEquals(requestDto.getEmail(), savedUser.getEmail());
    }

    @Test
    void 사장_사용가능_이메일_회원가입() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto("test@gmail.com", "Password123##", "김사장", "010-1234-5678", UserRole.OWNER);
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(jwtTokenProvider.generateAccessToken(anyString(), anyString())).thenReturn("mockAccessToken");

        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setRefreshToken("mockRefreshToken");
        when(refreshTokenService.createRefreshToken(requestDto.getEmail())).thenReturn(mockRefreshToken);

        ArgumentCaptor<User> ownerCaptor = ArgumentCaptor.forClass(User.class);

        // when
        TokenResponseDto responseDto = userService.signUp(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("mockAccessToken", responseDto.getAccessToken());
        assertEquals("mockRefreshToken", responseDto.getRefreshToken());
        verify(userRepository).existsByEmail(requestDto.getEmail());
        verify(passwordEncoder).encode(requestDto.getPassword());
        verify(userRepository).save(ownerCaptor.capture());
        verify(refreshTokenService).createRefreshToken(requestDto.getEmail());

        User savedOwner = ownerCaptor.getValue();
        assertEquals(requestDto.getEmail(), savedOwner.getEmail());
        assertEquals("encodedPassword", savedOwner.getPassword());
        assertEquals(requestDto.getName(), savedOwner.getName());
        assertEquals(requestDto.getPhoneNumber(), savedOwner.getPhoneNumber());
        assertEquals(requestDto.getEmail(), savedOwner.getEmail());
    }

    @Test
    void 회원_로그인성공() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@gmail.com");
        requestDto.setPassword("Password123##");

        User mockUser = new User("test@gmail.com", "encodedPassword", "김배민", "010-1234-5678", UserRole.USER);

        when(userRepository.findByEmailAndIsDeletedFalse(requestDto.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(requestDto.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(anyString(), anyString())).thenReturn("mockAccessToken");

        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setRefreshToken("mockRefreshToken");
        when(refreshTokenService.createRefreshToken(requestDto.getEmail())).thenReturn(mockRefreshToken);

        // when
        TokenResponseDto responseDto = userService.login(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("mockAccessToken", responseDto.getAccessToken());
        assertEquals("mockRefreshToken", responseDto.getRefreshToken());
        verify(userRepository).findByEmailAndIsDeletedFalse(requestDto.getEmail());
        verify(passwordEncoder).matches(requestDto.getPassword(), mockUser.getPassword());
        verify(jwtTokenProvider).generateAccessToken(mockUser.getEmail(), mockUser.getUserRole().name());
        verify(refreshTokenService).createRefreshToken(requestDto.getEmail());
    }

    @Test
    void 사장_로그인성공() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@gmail.com");
        requestDto.setPassword("Password123##");

        User mockOwner = new User("test@gmail.com", "encodedPassword", "김사장", "010-1234-5678", UserRole.OWNER);

        when(userRepository.findByEmailAndIsDeletedFalse(requestDto.getEmail())).thenReturn(Optional.of(mockOwner));
        when(passwordEncoder.matches(requestDto.getPassword(), mockOwner.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(anyString(), anyString())).thenReturn("mockAccessToken");

        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setRefreshToken("mockRefreshToken");
        when(refreshTokenService.createRefreshToken(requestDto.getEmail())).thenReturn(mockRefreshToken);

        // when
        TokenResponseDto responseDto = userService.login(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("mockAccessToken", responseDto.getAccessToken());
        assertEquals("mockRefreshToken", responseDto.getRefreshToken());
        verify(userRepository).findByEmailAndIsDeletedFalse(requestDto.getEmail());
        verify(passwordEncoder).matches(requestDto.getPassword(), mockOwner.getPassword());
        verify(jwtTokenProvider).generateAccessToken(mockOwner.getEmail(), mockOwner.getUserRole().name());
        verify(refreshTokenService).createRefreshToken(requestDto.getEmail());
    }

    @Test
    void 로그인실패_잘못된_이메일() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@gmail.com");
        requestDto.setPassword("Password123##");

        when(userRepository.findByEmailAndIsDeletedFalse(requestDto.getEmail())).thenReturn(Optional.empty());

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.login(requestDto));

        // then
        assertEquals("이메일이나 비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(userRepository).findByEmailAndIsDeletedFalse(requestDto.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void 회원_로그인실패_잘못된_비밀번호() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@gmail.com");
        requestDto.setPassword("Password123##");

        User mockUser = new User("test@gmail.com", "encodedPassword", "김배민", "010-1234-5678", UserRole.USER);

        when(userRepository.findByEmailAndIsDeletedFalse(requestDto.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(requestDto.getPassword(), mockUser.getPassword())).thenReturn(false);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.login(requestDto));

        // then
        assertEquals("이메일이나 비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(userRepository).findByEmailAndIsDeletedFalse(requestDto.getEmail());
        verify(passwordEncoder).matches(requestDto.getPassword(), mockUser.getPassword());
    }

    @Test
    void 사장_로그인실패_잘못된_비밀번호() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@gmail.com");
        requestDto.setPassword("Password123##");

        User mockOwner = new User("test@gmail.com", "encodedPassword", "김사장", "010-1234-5678", UserRole.OWNER);

        when(userRepository.findByEmailAndIsDeletedFalse(requestDto.getEmail())).thenReturn(Optional.of(mockOwner));
        when(passwordEncoder.matches(requestDto.getPassword(), mockOwner.getPassword())).thenReturn(false);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.login(requestDto));

        // then
        assertEquals("이메일이나 비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(userRepository).findByEmailAndIsDeletedFalse(requestDto.getEmail());
        verify(passwordEncoder).matches(requestDto.getPassword(), mockOwner.getPassword());
    }

    @Test
    void 회원정보_수정_성공() {
        // given
        String token = "mockToken";
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("test@gmail.com");
        when(jwtTokenProvider.parseClaims(token)).thenReturn(claims);

        User mockUser = new User("test@gmail.com", "encodedPassword", "김배민", "010-1234-5678", UserRole.USER);
        when(userRepository.findByEmailAndIsDeletedFalse("test@gmail.com")).thenReturn(Optional.of(mockUser));

        UserRequestDto requestDto = new UserRequestDto(null, "NewPassword123##", "김쿠팡이츠", "010-5678-1234", null);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("newEncodedPassword");

        // when
        UserResponseDto responseDto = userService.updateUser(token, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("test@gmail.com", responseDto.getEmail());
        assertEquals("김쿠팡이츠", responseDto.getName());
        assertEquals("010-5678-1234", responseDto.getPhoneNumber());

        verify(jwtTokenProvider).parseClaims(token);
        verify(userRepository).findByEmailAndIsDeletedFalse("test@gmail.com");
        verify(passwordEncoder).encode(requestDto.getPassword());
    }

    @Test
    void 사장정보_수정_성공() {
        // given
        String token = "mockToken";
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("test@gmail.com");
        when(jwtTokenProvider.parseClaims(token)).thenReturn(claims);

        User mockOwner = new User("test@gmail.com", "encodedPassword", "김사장", "010-1234-5678", UserRole.OWNER);
        when(userRepository.findByEmailAndIsDeletedFalse("test@gmail.com")).thenReturn(Optional.of(mockOwner));

        UserRequestDto requestDto = new UserRequestDto(null, "NewPassword123##", "김오너", "010-5678-1234", null);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("newEncodedPassword");

        // when
        UserResponseDto responseDto = userService.updateUser(token, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("test@gmail.com", responseDto.getEmail());
        assertEquals("김오너", responseDto.getName());
        assertEquals("010-5678-1234", responseDto.getPhoneNumber());

        verify(jwtTokenProvider).parseClaims(token);
        verify(userRepository).findByEmailAndIsDeletedFalse("test@gmail.com");
        verify(passwordEncoder).encode(requestDto.getPassword());
    }

    @Test
    void 회원정보_수정_시_기존_비밀번호_유지() {
        // given
        String token = "mockToken";
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("test@gmail.com");
        when(jwtTokenProvider.parseClaims(token)).thenReturn(claims);

        User mockUser = new User("test@gmail.com", "encodedPassword", "김배민", "010-1234-5678", UserRole.USER);
        when(userRepository.findByEmailAndIsDeletedFalse("test@gmail.com")).thenReturn(Optional.of(mockUser));

        UserRequestDto requestDto = new UserRequestDto(null, null, "김쿠팡이츠", "010-5678-1234", null);

        // when
        UserResponseDto responseDto = userService.updateUser(token, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("test@gmail.com", responseDto.getEmail());
        assertEquals("김쿠팡이츠", responseDto.getName());
        assertEquals("010-5678-1234", responseDto.getPhoneNumber());

        verify(jwtTokenProvider).parseClaims(token);
        verify(userRepository).findByEmailAndIsDeletedFalse("test@gmail.com");
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void 사장정보_수정_시_기존_비밀번호_유지() {
        // given
        String token = "mockToken";
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("test@gmail.com");
        when(jwtTokenProvider.parseClaims(token)).thenReturn(claims);

        User mockOwner = new User("test@gmail.com", "encodedPassword", "김사장", "010-1234-5678", UserRole.OWNER);
        when(userRepository.findByEmailAndIsDeletedFalse("test@gmail.com")).thenReturn(Optional.of(mockOwner));

        UserRequestDto requestDto = new UserRequestDto(null, null, "김오너", "010-5678-1234", null);

        // when
        UserResponseDto responseDto = userService.updateUser(token, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("test@gmail.com", responseDto.getEmail());
        assertEquals("김오너", responseDto.getName());
        assertEquals("010-5678-1234", responseDto.getPhoneNumber());

        verify(jwtTokenProvider).parseClaims(token);
        verify(userRepository).findByEmailAndIsDeletedFalse("test@gmail.com");
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void 존재하지_않는_회원정보_수정_실패() {
        String token = "mockToken";
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("test@gmail.com");
        when(jwtTokenProvider.parseClaims(token)).thenReturn(claims);

        when(userRepository.findByEmailAndIsDeletedFalse("test@gmail.com")).thenReturn(Optional.empty());

        UserRequestDto requestDto = new UserRequestDto(null, "NewPassword123##", "김쿠팡이츠", "010-5678-1234", null);

        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.updateUser(token, requestDto));

        // then
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
        verify(jwtTokenProvider).parseClaims(token);
        verify(userRepository).findByEmailAndIsDeletedFalse("test@gmail.com");
    }

    @Test
    void 회원탈퇴_성공() {
        //given
        String userEmail = "test@gmail.com";
        String rawPassword = "Password123##";
        User mockUser = new User(userEmail, "encodedPassword", "김배민", "010-1234-5678", UserRole.USER);

        when(userRepository.findByEmailAndIsDeletedFalse(userEmail)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(rawPassword, mockUser.getPassword())).thenReturn(true);

        // when
        userService.deleteUser(userEmail, rawPassword);

        // then
        assertTrue(mockUser.isDeleted());
        verify(userRepository).save(mockUser);
        verify(refreshTokenService).deleteByEmail(userEmail);
    }

    @Test
    void 사장_회원탈퇴_성공() {
        //given
        String userEmail = "test@gmail.com";
        String rawPassword = "Password123##";
        User mockOwner = new User(userEmail, "encodedPassword", "김사장", "010-1234-5678", UserRole.OWNER);

        when(userRepository.findByEmailAndIsDeletedFalse(userEmail)).thenReturn(Optional.of(mockOwner));
        when(passwordEncoder.matches(rawPassword, mockOwner.getPassword())).thenReturn(true);

        // when
        userService.deleteUser(userEmail, rawPassword);

        // then
        assertTrue(mockOwner.isDeleted());
        verify(userRepository).save(mockOwner);
        verify(refreshTokenService).deleteByEmail(userEmail);
    }

    @Test
    void 회원탈퇴_실패_비밀번로불일치() {
        // given
        String userEmail = "test@gmail.com";
        String rawPassword = "Password123##";
        User mockUser = new User(userEmail, "encodedPassword", "김배민", "010-1234-5678", UserRole.USER);

        when(userRepository.findByEmailAndIsDeletedFalse(userEmail)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(rawPassword, mockUser.getPassword())).thenReturn(false);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userEmail, rawPassword));

        // then
        assertEquals("이메일이나 비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    void 사장탈퇴_실패_비밀번로불일치() {
        // given
        String userEmail = "test@gmail.com";
        String rawPassword = "Password123##";
        User mockOwner = new User(userEmail, "encodedPassword", "김사장", "010-1234-5678", UserRole.OWNER);

        when(userRepository.findByEmailAndIsDeletedFalse(userEmail)).thenReturn(Optional.of(mockOwner));
        when(passwordEncoder.matches(rawPassword, mockOwner.getPassword())).thenReturn(false);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userEmail, rawPassword));

        // then
        assertEquals("이메일이나 비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    void 회원_AccessToken_재발급_성공() {
        // given
        String refreshTokenValue = "validRefreshToken";
        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setRefreshToken(refreshTokenValue);
        mockRefreshToken.setEmail("test@gmail.com");

        User mockUser = new User("test@gmail.com", "encodedPassword", "김배민", "010-1234-5678", UserRole.USER);

        when(jwtTokenProvider.validateToken(refreshTokenValue)).thenReturn(true);
        when(refreshTokenService.getByToken(refreshTokenValue)).thenReturn(Optional.of(mockRefreshToken));
        when(refreshTokenService.verifyExpiration(mockRefreshToken)).thenReturn(mockRefreshToken);
        when(userRepository.findByEmailAndIsDeletedFalse(mockRefreshToken.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtTokenProvider.generateAccessToken(mockUser.getEmail(), mockUser.getUserRole().name())).thenReturn("newAccessToken");

        // when
        TokenResponseDto responseDto = userService.reissueAccessToken(refreshTokenValue);

        // then
        assertNotNull(responseDto);
        assertEquals("newAccessToken", responseDto.getAccessToken());
        assertEquals(refreshTokenValue, responseDto.getRefreshToken());
    }

    @Test
    void 사장_AccessToken_재발급_성공() {
        // given
        String refreshTokenValue = "validRefreshToken";
        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setRefreshToken(refreshTokenValue);
        mockRefreshToken.setEmail("test@gmail.com");

        User mockOwner = new User("test@gmail.com", "encodedPassword", "김사장", "010-1234-5678", UserRole.OWNER);

        when(jwtTokenProvider.validateToken(refreshTokenValue)).thenReturn(true);
        when(refreshTokenService.getByToken(refreshTokenValue)).thenReturn(Optional.of(mockRefreshToken));
        when(refreshTokenService.verifyExpiration(mockRefreshToken)).thenReturn(mockRefreshToken);
        when(userRepository.findByEmailAndIsDeletedFalse(mockRefreshToken.getEmail())).thenReturn(Optional.of(mockOwner));
        when(jwtTokenProvider.generateAccessToken(mockOwner.getEmail(), mockOwner.getUserRole().name())).thenReturn("newAccessToken");

        // when
        TokenResponseDto responseDto = userService.reissueAccessToken(refreshTokenValue);

        // then
        assertNotNull(responseDto);
        assertEquals("newAccessToken", responseDto.getAccessToken());
        assertEquals(refreshTokenValue, responseDto.getRefreshToken());
    }

    @Test
    void AccessToken_재발급_실패_만료된토믄() {
        String refreshTokenValue = "expiredRefreshToken";

        when(jwtTokenProvider.validateToken(refreshTokenValue)).thenReturn(false);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.reissueAccessToken(refreshTokenValue));

        // then
        assertEquals("유효하지 않은 Refresh Token입니다.", exception.getMessage());
    }

    @Test
    void AccessToken_재발급_실패_존재하지_않는_토큰() {
        // given
        String refreshTokenValue = "noexistRefreshToken";

        when(jwtTokenProvider.validateToken(refreshTokenValue)).thenReturn(true);
        when(refreshTokenService.getByToken(refreshTokenValue)).thenReturn(Optional.empty());

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.reissueAccessToken(refreshTokenValue));

        // then
        assertEquals("Refresh Token을 찾을 수 없습니다.", exception.getMessage());
    }
}