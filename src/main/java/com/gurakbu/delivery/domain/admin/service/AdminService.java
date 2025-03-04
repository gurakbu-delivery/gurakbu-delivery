package com.gurakbu.delivery.domain.admin.service;

import com.gurakbu.delivery.config.PasswordEncoder;
import com.gurakbu.delivery.domain.admin.dto.request.AdminRequestDto;
import com.gurakbu.delivery.domain.admin.dto.response.AdminResponseDto;
import com.gurakbu.delivery.domain.admin.entity.Admin;
import com.gurakbu.delivery.domain.admin.repository.AdminRepository;
import com.gurakbu.delivery.domain.user.dto.response.UserResponseDto;
import com.gurakbu.delivery.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminResponseDto createAdmin(AdminRequestDto adminRequestDto) {
        String encodedPassword = passwordEncoder.encode(adminRequestDto.getPassword());
        Admin admin = new Admin(adminRequestDto.getEmail(), encodedPassword, adminRequestDto.getName(), adminRequestDto.getPhone(), adminRequestDto.getRole());
        Admin createdAdmin = adminRepository.save(admin);

        return new AdminResponseDto(createdAdmin.getId(), createdAdmin.getEmail(),
                encodedPassword, createdAdmin.getName(), createdAdmin.getPhone(), createdAdmin.getRole());
    }

    @Transactional(readOnly = true)
    public AdminResponseDto loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(
                () -> new IllegalStateException("해당 이메일은 가입되지 않았습니다.")
        );

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        return new AdminResponseDto(admin.getId(), admin.getEmail(), admin.getPassword(), admin.getName(), admin.getPhone(), admin.getRole());
    }

    @Transactional
    public AdminResponseDto updateAdmin(Long id, AdminRequestDto adminRequestDto) {
        Admin admin = adminRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("해당 id의 관리자가 존재하지 않습니다.")
        );

        admin.update(adminRequestDto.getEmail(), adminRequestDto.getPassword(), adminRequestDto.getName(), adminRequestDto.getPhone());
        return new AdminResponseDto(admin.getId(), admin.getEmail(), admin.getPassword(), admin.getName(), admin.getPhone(), admin.getRole());
    }

    @Transactional
    public void deleteAdmin(Long id) {
        boolean b = adminRepository.existsById(id);
        if (!b) {
            throw new IllegalStateException("해당 id의 관리자가 존재하지 않습니다.");
        }

        adminRepository.deleteById(id);
    }
}
