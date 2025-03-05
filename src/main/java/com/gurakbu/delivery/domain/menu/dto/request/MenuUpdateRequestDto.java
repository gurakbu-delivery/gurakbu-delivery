package com.gurakbu.delivery.domain.menu.dto.request;

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
public class MenuUpdateRequestDto { // 수정하고 싶은 값만 입력 (null 허용)
    private String name;            // 메뉴명
    @Min(value = 0, message = "[가격범위오류] 가격은 0 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "[가격범위오류] 가격은 2,147,483,647원을 초과할 수 없습니다.")
    private Integer price;          // 메뉴가격
    private MenuCategory category;  // 메뉴 카테고리
    private String description;     // 메뉴 설명
    private Boolean popularity;     // 인기(추천) 메뉴
    private MenuStatus status;      // 메뉴 상테
}