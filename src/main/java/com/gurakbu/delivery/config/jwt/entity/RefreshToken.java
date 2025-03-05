package com.gurakbu.delivery.config.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private Instant expiryDate;

    public RefreshToken(String email, String refreshToken, Instant expiryDate) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expiryDate = expiryDate;
    }

}
