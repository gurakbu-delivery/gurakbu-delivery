package com.gurakbu.delivery.domain.restaurant.dto.request;

import com.gurakbu.delivery.domain.restaurant.enums.RestaurantCategory;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class RestaurantUpdateRequestDto {
    private String name;
    private String address;
    private String description;
    private RestaurantCategory category;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer minDeliveryPrice;
}
