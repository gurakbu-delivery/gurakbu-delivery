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

    @NotBlank(message = "이름입력ㄱㄱ")
    private String name;

    @NotBlank(message = "주소입력ㄱㄱ")
    private String address;

    private String description;

    @NotNull(message = "카테고리선택ㄱㄱ")
    private RestaurantCategory category;

    @NotNull(message = "상태입력ㄱㄱ")
    private RestaurantStatus status;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    private Integer minDeliveryPrice;

}
