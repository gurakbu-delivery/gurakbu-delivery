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
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // 가게 생성: 한 사장님당 최대 3개까지 생성 가능
    @Transactional
    public RestaurantDetailResponseDto createRestaurant(User user, RestaurantCreateRequestDto requestDto) {
        // 사장님이 이미 생성한 가게 개수 제한
        long count = restaurantRepository.countByUserId(user.getId());
        if(count >= 3) {
            throw new IllegalArgumentException("한 사장님은 최대 3개의 가게만 생성할 수 있습니다.");
        }
        // Restaurant 엔티티 생성 및 소유자 설정
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
        restaurant.setUser(user); // 소유자 할당
        restaurantRepository.save(restaurant);
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }

    // 가게 수정: 요청한 사용자가 소유자인지 확인
    @Transactional
    public RestaurantDetailResponseDto updateRestaurant(User user, Long id, RestaurantUpdateRequestDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        if (!restaurant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        restaurant.updateRestaurant(dto);
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }

    // 가게 단건 조회: CLOSED 상태인 가게는 조회 불가하도록 처리
    @Transactional(readOnly = true)
    public RestaurantDetailResponseDto findRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        if (restaurant.getStatus() == RestaurantStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 가게는 조회할 수 없습니다.");
        }
        return RestaurantDetailResponseDto.fromEntity(restaurant);
    }

    // 가게 목록 조회: CLOSED 상태인 가게는 목록에서 제외
    @Transactional(readOnly = true)
    public List<RestaurantListResponseDto> findAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll()
                .stream()
                .filter(r -> r.getStatus() != RestaurantStatus.CLOSED)
                .toList();
        return restaurants.stream().map(RestaurantListResponseDto::fromEntity).toList();
    }

    // 가게 폐업: 요청한 사용자가 소유자인지 확인
    @Transactional
    public void closeRestaurant(User user, Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));
        if (!restaurant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "폐업 권한이 없습니다.");
        }
        restaurant.close();
    }
}