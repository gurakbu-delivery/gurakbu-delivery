package com.gurakbu.delivery.domain.restaurant.service;

import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantListResponseDto;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import com.gurakbu.delivery.domain.user.entity.User;
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
    public RestaurantDetailResponseDto createRestaurant(User user, RestaurantCreateRequestDto requestDto) {
        if(!user.isOwner(user.getId())){
            throw new SecurityException("권한이 없습니다.");
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
        List<RestaurantListResponseDto> dtos = restaurantRepository.findAll()
                .stream().map(RestaurantListResponseDto::fromEntity).toList();
        return dtos;
    }

    @Transactional
    public RestaurantDetailResponseDto updateRestaurant(User user, Long id, RestaurantUpdateRequestDto requestDto) {
        if(!user.isOwner(user.getId())){
            throw new SecurityException("권한이 없습니다.");
        }
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        restaurant.updateRestaurant(user, requestDto);
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }

    @Transactional
    public void closeRestaurant(User user, Long id) {
        if(!user.isOwner(user.getId())){
            throw new SecurityException("권한이 없습니다.");
        }
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        restaurant.close();
    }
}
