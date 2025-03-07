package com.gurakbu.delivery.domain.user.entity;

import com.gurakbu.delivery.common.BaseTimeEntity;
import com.gurakbu.delivery.domain.order.entity.Order;
import com.gurakbu.delivery.domain.review.entity.Review;
import com.gurakbu.delivery.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public User(String email, String password, String name, String phoneNumber, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userRole = role;
    }

    public User(String email, String password, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    // 회원 정보 업데이트
    public void update(String password, String name, String phoneNumber) {
        if (password != null && !password.isBlank()) {
            this.password = password;
        }
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            this.phoneNumber = phoneNumber;
        }
    }

    // UserRole을 통한 ADMIN 여부 확인
    public boolean isAdmin() {
        return this.userRole.isAdmin();
    }

    // UserRole을 통한 OWNER 여부 확인
    public boolean isOwner(Long restaurantId) {
        // 간단하게 OWNER 역할만 체크하거나,
        // 추가로 해당 사용자가 실제로 해당 식당의 소유자인지 검증하는 로직을 추가할 수 있습니다.
        return this.userRole.isOwner();
    }

}
