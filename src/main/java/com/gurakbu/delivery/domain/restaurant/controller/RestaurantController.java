package com.gurakbu.delivery.domain.restaurant.controller;

import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * 가게 생성 요청
     * -> 사장님만 생성 가능하도록 수정해야 함(사용자 정보 추가), 서비스 로직에 예외처리도 필요
     *
     * @param requestDto
     * @return RestaurantDetailResponseDto, Status 201
     */
    @PostMapping
    public ResponseEntity<RestaurantDetailResponseDto> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequestDto requestDto
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.createRestaurant(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 가게 정보 수정 요청
     * -> 권한 검증 로직 필요, 로그인 정보로 사용자 받아오면 id 제거
     *
     * @param id
     * @param requestDto
     * @return RestaurantDetailResponseDto, Status 200
     */
    @PutMapping("{id}")
    public ResponseEntity<RestaurantDetailResponseDto> updateRestaurant(
            @PathVariable Long id, @RequestBody RestaurantUpdateRequestDto requestDto
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.updateRestaurant(id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
