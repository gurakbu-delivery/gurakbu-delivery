package com.gurakbu.delivery.domain.restaurant.dto.response;

import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantListResponseDto {
    private final Long id;
    private final String name;
    private final Integer minDeliveryPrice;

    public static RestaurantListResponseDto fromEntity(Restaurant restaurant){
        return RestaurantListResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .minDeliveryPrice(restaurant.getMinDeliveryPrice())
                .build();
    }
}
