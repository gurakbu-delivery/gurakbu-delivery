package com.gurakbu.delivery.domain.admin.controller;

import com.gurakbu.delivery.domain.admin.dto.request.AdminRequestDto;
import com.gurakbu.delivery.domain.admin.dto.response.AdminResponseDto;
import com.gurakbu.delivery.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admins")
    public ResponseEntity<AdminResponseDto> createAdmin(@RequestBody AdminRequestDto adminRequestDto) {
        return ResponseEntity.ok(adminService.createAdmin(adminRequestDto));
    }

    @PostMapping("/admins/login")
    public ResponseEntity<AdminResponseDto> loginAdmin(@RequestBody AdminRequestDto adminRequestDto) {
        return ResponseEntity.ok(adminService.loginAdmin(adminRequestDto.getEmail(), adminRequestDto.getPassword()));
    }

    @PutMapping("/admins/{id}")
    public ResponseEntity<AdminResponseDto> updateAdmin(@PathVariable Long id, @RequestBody AdminRequestDto adminRequestDto) {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminRequestDto));
    }

    @DeleteMapping("/admins/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
    }
}
