package com.gurakbu.delivery.domain.restaurant.service;

import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantListResponseDto;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import com.gurakbu.delivery.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantDetailResponseDto createRestaurant(User user, RestaurantCreateRequestDto requestDto) {
        if(!user.isOwner(user.getId())){
            throw new SecurityException("권한없음");
        }
        long openRestaurants = restaurantRepository.countByUserIdAndStatus(user.getId(), RestaurantStatus.OPEN);
        if (openRestaurants >= 3) {
            throw new IllegalStateException("가게는 3개까지 생성 가능");
        }
        Restaurant restaurant = new Restaurant(
                user,
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

    @Transactional(readOnly = true)
    public RestaurantDetailResponseDto findRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantListResponseDto> findAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.getRestaurantsByCategory(RestaurantStatus.CLOSED);
        return restaurants.stream().map(RestaurantListResponseDto::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<RestaurantListResponseDto> getRestaurantsByCategory(String category) {
        List<Restaurant> restaurants = restaurantRepository.findAllByCategoryAndStatusNotOrderByUpdatedAtDesc(category, RestaurantStatus.CLOSED);
        return restaurants.stream()
                .map(RestaurantListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDetailResponseDto updateRestaurant(User user, Long id, RestaurantUpdateRequestDto requestDto) {
        if(!user.isOwner(user.getId())){
            throw new SecurityException("권한없음");
        }
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        restaurant.updateRestaurant(user, requestDto);
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }

    @Transactional
    public void closeRestaurant(User user, Long id) {
        if(!user.isOwner(user.getId())){
            throw new SecurityException("권한없음");
        }
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        restaurant.close();
    }
}
