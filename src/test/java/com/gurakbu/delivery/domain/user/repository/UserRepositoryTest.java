package com.gurakbu.delivery.domain.user.repository;

import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void 이메일로_회원조회() {
        // given
        String email = "test@gmail.com";
        User user = new User(email, "Password123", "김배민", "010-1234-5678", UserRole.USER);
        userRepository.save(user);

        // when
        User foundUser = userRepository.findByEmailAndIsDeletedFalse(email).orElse(null);

        // then
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        assertEquals(UserRole.USER, foundUser.getUserRole());
    }

    @Test
    void 회원탈퇴한_이메일로_회원조회() {
        // given
        String email = "test@gmail.com";
        User user = new User(email, "Password123", "김배민", "010-1234-5678", UserRole.USER);
        user.setDeleted(true);
        userRepository.save(user);

        // when
        User foundUser = userRepository.findByEmailAndIsDeletedFalse(email).orElse(null);

        // then
        assertNull(foundUser);
    }

    @Test
    void 이미_가입된_이메일일때_true() {
        // given
        String email = "exist@gmail.com";
        User user = new User(email, "Password123", "김배민", "010-1234-5678", UserRole.USER);
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmail(email);

        // then
        assertTrue(exists, "이미 가입된 사용자 이메일입니다.");
    }

    @Test
    void 이미_가입된_이메일일때_false() {
        // given
        String email = "notExist@gmail.com";

        // when
        boolean exists = userRepository.existsByEmail(email);

        // then
        assertFalse(exists, "사용가능한 이메일입니다.");

    }

}