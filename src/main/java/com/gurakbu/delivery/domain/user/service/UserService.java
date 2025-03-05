package com.gurakbu.delivery.domain.user.service;

import com.gurakbu.delivery.config.JwtTokenProvider;
import com.gurakbu.delivery.config.PasswordEncoder;
import com.gurakbu.delivery.domain.user.dto.request.UserRequestDto;
import com.gurakbu.delivery.domain.user.dto.response.UserResponseDto;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.enums.Role;
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

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        Role role = (userRequestDto.getRole() != null) ? userRequestDto.getRole() : Role.USER;  // 요청된 role이 없으면 기본값 USER

        User user = new User(userRequestDto.getEmail(), encodedPassword, userRequestDto.getName(), userRequestDto.getPhone(), role);
        User createdUser = userRepository.save(user);

        return new UserResponseDto(createdUser.getId(), createdUser.getEmail(), createdUser.getName(), createdUser.getPhone(), createdUser.getRole());
    }

    @Transactional(readOnly = true)
    public UserResponseDto loginUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalStateException("해당 이메일은 가입되지 않았습니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.createToken(user.getEmail(), user.getRole());
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("해당 id의 회원이 존재하지 않습니다.")
        );

        String newPassword = userRequestDto.getPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            newPassword = passwordEncoder.encode(newPassword);
        } else {
            newPassword = user.getPassword();
        }

        user.update(newPassword, userRequestDto.getName(), userRequestDto.getPhone());
        return new UserResponseDto(user.getId(), user.getEmail(), user.getName(), user.getPhone(), user.getRole());
    }

    @Transactional
    public void deleteUser(Long id) {
        boolean b = userRepository.existsById(id);
        if (!b) {
            throw new IllegalStateException("해당 id의 회원이 존재하지 않습니다.");
        }

        userRepository.deleteById(id);
    }
}
