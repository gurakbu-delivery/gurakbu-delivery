package com.gurakbu.delivery.domain.restaurant.service;

import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantDetailResponseDto createRestaurant(RestaurantCreateRequestDto requestDto) {
        Restaurant restaurant = new Restaurant(
                requestDto.getName(),
                requestDto.getAddress(),
                requestDto.getDescription(),
                requestDto.getCategory(),
                requestDto.getStatus(),
                requestDto.getOpenTime(),
                requestDto.getCloseTime(),
                requestDto.getMinDeliveryPrice()
        );
        restaurantRepository.save(restaurant);
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }
}
