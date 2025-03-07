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
@Builder
public class MenuCreateRequestDto {
    // 메뉴명
    @NotBlank(message = "[필수값누락] 메뉴명을 입력하세요.")
    private String name;

    // 메뉴가격
    @NotNull(message = "[필수값누락] 가격을 입력하세요.")
    @Min(value = 0, message = "[가격범위오류] 가격은 0 이상이어야 합니다.")
    @Max(value = Integer.MAX_VALUE, message = "[가격범위오류] 가격은 2,147,483,647원을 초과할 수 없습니다.")
    private Integer price;

    // 이외 선택입력 필드(미입력시 기본값 설정) - Entity @PrePersist로 더블체크함으로써 API를 통해 저장할 때와 DB에서 직접 저장할 때 모두 일관성 유지
    // 메뉴 카테고리      / 기본값 "ETC"
    @Builder.Default
    private MenuCategory category = MenuCategory.ETC;

    // 메뉴 설명        / NULL 허용
    private String description;

    // 인기(추천) 메뉴    / 기본값 "FALSE"
    @Builder.Default
    private Boolean popularity = false;

    // 메뉴 상테         / 기본값 "CLOSED"
    @Builder.Default
    private MenuStatus status = MenuStatus.CLOSED;
}
