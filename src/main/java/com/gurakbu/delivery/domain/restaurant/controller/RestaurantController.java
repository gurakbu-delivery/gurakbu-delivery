package com.gurakbu.delivery.domain.restaurant.controller;

import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantListResponseDto;
import com.gurakbu.delivery.domain.restaurant.service.RestaurantService;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    // 가게 생성 (인증된 사장님만 가능)
    @PostMapping
    public ResponseEntity<RestaurantDetailResponseDto> createRestaurant(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody RestaurantCreateRequestDto requestDto
    ){
        // 인증된 사용자 조회
        User user = userService.findByEmail(email);
        RestaurantDetailResponseDto responseDto = restaurantService.createRestaurant(user, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 가게 수정 (해당 가게의 소유자만 가능)
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> updateRestaurant(
            @AuthenticationPrincipal String email,
            @PathVariable Long id,
            @RequestBody RestaurantUpdateRequestDto requestDto
    ){
        User user = userService.findByEmail(email);
        RestaurantDetailResponseDto responseDto = restaurantService.updateRestaurant(user, id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 가게 단건 조회 (CLOSED 상태인 경우 조회 불가)
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> findRestaurant(@PathVariable Long id){
        RestaurantDetailResponseDto responseDto = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 가게 목록 조회 (CLOSED 상태인 가게는 조회되지 않음)
    @GetMapping
    public ResponseEntity<List<RestaurantListResponseDto>> findRestaurants(){
        List<RestaurantListResponseDto> dtos = restaurantService.findAllRestaurants();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // 가게 폐업 (해당 가게의 소유자만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> closeRestaurant(
            @AuthenticationPrincipal String email,
            @PathVariable Long id
    ){
        User user = userService.findByEmail(email);
        restaurantService.closeRestaurant(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
