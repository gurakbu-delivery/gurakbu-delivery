package com.gurakbu.delivery.domain.restaurant.service;

import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantListResponseDto;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
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

    @Transactional
    public RestaurantDetailResponseDto updateRestaurant(Long id, RestaurantUpdateRequestDto requestDto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        restaurant.updateRestaurant(requestDto);
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }

    @Transactional(readOnly = true)
    public RestaurantDetailResponseDto findRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantListResponseDto> findAllRestaurants() {
        List<RestaurantListResponseDto> dtos = restaurantRepository.findAll()
                .stream().map(RestaurantListResponseDto::fromEntity).toList();
        return dtos;
    }
}
