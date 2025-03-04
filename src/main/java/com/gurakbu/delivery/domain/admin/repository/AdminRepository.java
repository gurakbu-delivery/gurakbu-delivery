package com.gurakbu.delivery.domain.admin.repository;

import com.gurakbu.delivery.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> updateById(Long id);
}
