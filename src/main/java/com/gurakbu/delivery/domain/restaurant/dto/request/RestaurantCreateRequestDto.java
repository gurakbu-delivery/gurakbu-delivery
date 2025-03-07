package com.gurakbu.delivery.domain.restaurant.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantCategory;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class RestaurantCreateRequestDto {

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotBlank(message = "주소를 입력하세요.")
    private String address;

    private String description;

    @NotNull(message = "카테고리를 선택하세요.")
    private RestaurantCategory category;

    @NotNull(message = "상태를 입력하세요.")
    private RestaurantStatus status;

    @NotNull(message = "오픈 시간을 입력하세요.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @NotNull(message = "마감 시간을 입력하세요.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @NotNull(message = "최소 배달 가격을 입력하세요.")
    private Integer minDeliveryPrice;
}

