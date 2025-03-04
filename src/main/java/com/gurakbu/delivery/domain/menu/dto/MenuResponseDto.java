package com.gurakbu.delivery.domain.menu.dto;

import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.entity.MenuCategory;
import com.gurakbu.delivery.domain.menu.entity.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MenuResponseDto {
    private Long id;
    private String name;
    private Integer price;
    private MenuCategory category;
    private String description;
    private Boolean popularity;
    private MenuStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 생성자: 엔티티 -> DTO로 변환
    public MenuResponseDto( Long id,
                                  String name,
                                  Integer price,
                                  MenuCategory category,
                                  String description,
                                  Boolean popularity,
                                  MenuStatus status,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.popularity = popularity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 정적 메서드로 DTO변환
    public static MenuResponseDto fromEntity(Menu menu) {
        return new MenuResponseDto(menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getCategory(),
                menu.getDescription(),
                menu.getPopularity(),
                menu.getStatus(),
                menu.getCreatedAt(),
                menu.getUpdatedAt()
        );
    }
}
