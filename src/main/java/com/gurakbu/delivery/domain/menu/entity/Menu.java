package com.gurakbu.delivery.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity                     // 해당 클래스는 DB 클래스와 맵핑되는 JPA Entity 클래스
@Table(name="menus")        // DB 속 menus 테이블과 멥핑됨
@Getter                     // get 관련 메소드 자동 생성
@NoArgsConstructor          // 기본 생성자 자동 생성
@AllArgsConstructor         // 모든 필드를 포함하는 생성자도 자동 생성
@Builder(toBuilder = true)  // Builder 패턴 자동 생성 & 기존 객체 복사 후 수정 가능

public class Menu {
    // id : 기본 키, 자동생성, 자동증가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    아직 연동되지 않음
//    restaurent Entity와의 관계설정 : 외래 키
//    @ManyToOne
//    @JoinColumn(name = "restaurant_id", nullable = false) // menu의 단독조회가 불가능하므로 NOT NULL
//    private Restaurant restaurent;

    // 메뉴 이름
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    // 메뉴 가격
    // 약 21억 이하의 정수만을 다룸 / BigDecimal, Long 고민하였으나 메모리와 연산효율로 int 선택
    @Column(name = "price", nullable=false)
    private Integer price;

    // 메뉴 카테고리 / NOT NULL
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable=false, length = 10)
    private MenuCategory category;

    // 메뉴 설명 / NULL
    // 데이터구조에는 있고 ERD에는 없어서 일단 구현함
    @Column(name = "description", length = 255)
    private String description;

    // 메뉴 인기여부
    @Column(name = "popularity", columnDefinition = "BIT")
    private Boolean popularity;

    // 메뉴 상태 / NOT NULL
    @Enumerated(EnumType.STRING)    // ENUM을 String 으로 저장
    @Column(name="status", length = 20)
    private MenuStatus status;

    // 메뉴 생성일자 / NULL, 자동생성 & 수정불가
    @Column(name ="created_at", updatable = false)
    private LocalDateTime createdAt;

    // 메뉴 수정일자
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    // 생성시 설정 : 생성시간, 수정시간 설정
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if(popularity == null) {        // 인기메뉴 미 입력시
            popularity = false;         // 기본값 설정
        }
        if (category == null) {          // 카테고리 미 입력시
            category = MenuCategory.ETC; // 기본값 설정
        }

        if (status == null) {                // 상태 미 입력시
            status = MenuStatus.UNAVAILABLE; // 기본값 설정
        }
    }

    // 수정시 설정 : 수정시간 재설정
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
