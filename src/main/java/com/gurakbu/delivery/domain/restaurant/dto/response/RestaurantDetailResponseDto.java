package com.gurakbu.delivery.domain.restaurant.dto.response;

import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantCategory;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
@Builder
public class RestaurantDetailResponseDto {

    private final Long id;
    private final String name;
    private final String address;
    private final String description;
    private final RestaurantCategory category;
    private final RestaurantStatus status;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final Integer minDeliveryPrice;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static RestaurantDetailResponseDto fromEntity(Restaurant restaurant){
        return RestaurantDetailResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .description(restaurant.getDescription())
                .category(restaurant.getCategory())
                .status(restaurant.getStatus())
                .openTime(restaurant.getOpenTime())
                .closeTime(restaurant.getCloseTime())
                .minDeliveryPrice(restaurant.getMinDeliveryPrice())
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .build();
    }
}