package com.gurakbu.delivery.domain.menu.dto.response;

import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.entity.MenuCategory;
import com.gurakbu.delivery.domain.menu.entity.MenuStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MenuResponseDto {
    private final Long id;
    private final String name;
    private final Integer price;
    private final MenuCategory category;
    private final String description;
    private final Boolean popularity;
    private final MenuStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // 정적 메서드로 DTO 변환 (Builder)
    public static MenuResponseDto fromEntity(Menu menu) {
        return MenuResponseDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .category(menu.getCategory())
                .description(menu.getDescription() != null ? menu.getDescription():"")
                .popularity(menu.getPopularity())
                .status(menu.getStatus())
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }
}