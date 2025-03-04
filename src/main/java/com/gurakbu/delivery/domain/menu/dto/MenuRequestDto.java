package com.gurakbu.delivery.domain.menu.dto;

import com.gurakbu.delivery.domain.menu.entity.MenuCategory;
import com.gurakbu.delivery.domain.menu.entity.MenuStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDto {
    @NotBlank(message = "[필수값누락] 메뉴명을 입력하세요.")
    private String name;

    @NotNull(message = "[필수값누락] 가격을 입력하세요.")
    @Min(value = 0, message = "[값오류] 가격은 0 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "[값초과] 가격은 2,147,483,647원을 초과할 수 없습니다.")
    private Integer price;

    private MenuCategory category;  // 메뉴 카테고리 / 미입력시 "ETC"
    private String description;     // 메뉴 설명    / NULL 가능
    private Boolean popularity;     // 인기메뉴     / 미입력시 "FALSE"
    private MenuStatus status;      // 메뉴 상테    / 미입력시 "UNAVAILABLE"

    // 완전 기본만 받는 경우 (필요없으면 삭제가능)
    public MenuRequestDto(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    // popularity 는 API 명세 requestbody에서 빠져있어서 allArgs 빼고 다른 생성자 만들어둠
    public MenuRequestDto(String name, Integer price, MenuCategory category, String description, MenuStatus status) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.status = status;
    }
}