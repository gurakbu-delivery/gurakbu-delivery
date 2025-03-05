package com.gurakbu.delivery.domain.menu.entity;

import com.gurakbu.delivery.common.BaseTimeEntity;
import com.gurakbu.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity                     // 해당 클래스는 DB 클래스와 맵핑되는 JPA Entity 클래스
@Table(name="menus")        // DB 속 menus 테이블과 멥핑됨
@Getter                     // get 관련 메소드 자동 생성
@NoArgsConstructor          // 기본 생성자 자동 생성
@AllArgsConstructor         // 모든 필드를 포함하는 생성자도 자동 생성
@Builder(toBuilder = true)  // Builder 패턴 자동 생성 & 기존 객체 복사 후 수정 가능

public class Menu extends BaseTimeEntity {
    // id : 기본 키, 자동생성, 자동증가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // restaurents Entity와의 관계설정 : 외래 키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false) // menu의 단독조회가 불가능하므로 NOT NULL
    private Long restaurentId;

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

    // 메뉴 인기여부 / NULL
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

    // 정적 팩토리 메서드: 메뉴 생성 로직(Menu.builder())을 엔티티 층으로 이동
    // 객체 생성에 필요한 값만 create에서 처리하고, 그 외 자동으로 보완할 값은 DB 저장 직전에 @PerPersist에서 처리
    public static Menu create(long restaurentId, String name, Integer price, MenuCategory category, String description, MenuStatus status, boolean popularity) {
        return Menu.builder()
                .restaurentId(restaurentId)
                .name(name)
                .price(price)
                .category(category != null ? category : MenuCategory.ETC)  // 기본값 설정
                .description(description)
                .status(status != null ? status : MenuStatus.CLOSED)        // 기본값 설정
                .popularity(popularity ? popularity : false)       // 기본값 설정
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // 객체 생성에 필요한 값만 create에서 처리하고, 그 외 자동으로 보완할 값은 DB 저장 직전에 @PerPersist에서 처리
    // 생성시 자동설정 : 생성시간, 수정시간
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();  // 기본값이 이미 설정되었지만, DB 저장 직전에 보장
        this.updatedAt = LocalDateTime.now();
    }

    // 메뉴 수정 메서드
   public void update(MenuUpdateRequestDto dto) {
       this.name = dto.getName() != null ? dto.getName() : this.name;
       this.price = dto.getPrice() != null ? dto.getPrice() : this.price;
       this.category = dto.getCategory() != null ? dto.getCategory() : this.category;
       this.description = dto.getDescription() != null ? dto.getDescription() : this.description;
       this.status = dto.getStatus() != null ? dto.getStatus() : this.status;
       this.popularity = dto.getPopularity() != null ? dto.getPopularity() : this.popularity;
       this.updatedAt = LocalDateTime.now();
   }

    // 메뉴 삭제
    public void delete(){
        this.status = MenuStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }
}

/*
id | BIGINT | N-N
restaurant_id | BIGINT | N-N
name | VARCHAR | N-N
price | INT | N-N
category | ENUM | N-N
popularity | BIT | N
status | ENUM | N-N
created_at | DATETIME | N
updated_at | DATETIME | N
 */