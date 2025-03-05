package com.gurakbu.delivery.config.jwt.service;

import com.gurakbu.delivery.config.jwt.JwtTokenProvider;
import com.gurakbu.delivery.config.jwt.entity.RefreshToken;
import com.gurakbu.delivery.config.jwt.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final long refreshTokenValidityMillis = 1000L * 60 * 60 * 24 * 7;

    @Transactional
    public RefreshToken createRefreshToken(String userEmail) {
        // 기존 토큰이 있는지 확인
        Optional<RefreshToken> maybeToken = refreshTokenRepository.findByEmail(userEmail);

        // 새 토큰 문자열 생성
        String token = jwtTokenProvider.generateRefreshToken(userEmail);
        Instant expiryDate = Instant.now().plusMillis(refreshTokenValidityMillis);

        // 있으면 업데이트, 없으면 새로 생성
        RefreshToken refreshToken = maybeToken.orElse(new RefreshToken());
        refreshToken.setEmail(userEmail);
        refreshToken.setRefreshToken(token);
        refreshToken.setExpiryDate(expiryDate);

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> getByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token has expired. Please sign in again.");
        }
        return token;
    }

    public void deleteByEmail(String userEmail) {
        refreshTokenRepository.deleteByEmail(userEmail);
    }
}
