package com.gurakbu.delivery.domain.restaurant.controller;

import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantListResponseDto;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.service.RestaurantService;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 가게 생성 (OWNER만 가능)
    @PostMapping
    public ResponseEntity<RestaurantDetailResponseDto> createRestaurant(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody RestaurantCreateRequestDto requestDto
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.createRestaurant(user, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 가게 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> findRestaurant(
            @PathVariable Long id
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 가게 목록 조회
    @GetMapping
    public ResponseEntity<List<RestaurantListResponseDto>> findRestaurants(){
        List<RestaurantListResponseDto> dtos = restaurantService.findAllRestaurants();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // 카테고리별 가게 조회
    @GetMapping("/category")
    public ResponseEntity<List<RestaurantListResponseDto>> findRestaurantsByCategory(
            @RequestParam String category) {
        List<RestaurantListResponseDto> response = restaurantService.getRestaurantsByCategory(category);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 가게 정보 수정 (OWNER만 가능)
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> updateRestaurant(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody RestaurantUpdateRequestDto requestDto
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.updateRestaurant(user, id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 가게 폐업 (OWNEW만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> closeRestaurant(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ){
        restaurantService.closeRestaurant(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
