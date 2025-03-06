package com.gurakbu.delivery.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequestDto {

    @NotBlank(message = "내용을 작성해주세요.")
    private String contents;

    @NotNull(message = "별점을 입력해주세요.")
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    @NotNull(message = "메뉴 ID를 입력해주세요.")
    private Long menuId;

    @NotNull(message = "주문 ID를 입력해주세요.")
    private Long orderId;

    @NotNull(message = "가게 ID를 입력해주세요.")
    private Long restaurantId;
}