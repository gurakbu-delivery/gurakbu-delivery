package com.gurakbu.delivery.config;

import com.gurakbu.delivery.config.jwt.JwtAuthenticationFilter;
import com.gurakbu.delivery.config.jwt.JwtTokenProvider;
import com.gurakbu.delivery.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService; // 필요 시 사용

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 회원가입, 로그인, 토큰재발급은 모두 접근 가능
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/signup", "/auth/reissue").permitAll()
                        // 회원정보 수정 모두 접근가능
                        .requestMatchers(HttpMethod.PUT, "/auth/users").permitAll()
                        // 회원탈퇴 모두 접근가능
                        .requestMatchers(HttpMethod.DELETE, "/auth/delete").permitAll()
                        // 그 외는 인증 필요
                        .anyRequest().authenticated()
                )
                // JwtAuthenticationFilter 등록
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        // principal을 String email로 세팅하는 필터
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }
}
