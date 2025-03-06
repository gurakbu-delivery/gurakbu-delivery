package com.gurakbu.delivery.config.jwt.repository;

import com.gurakbu.delivery.config.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByEmail(String email);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteByEmail(String email);

}
