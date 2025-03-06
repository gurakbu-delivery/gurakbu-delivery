package com.gurakbu.delivery.domain.restaurant.controller;

import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantListResponseDto;
import com.gurakbu.delivery.domain.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDetailResponseDto> createRestaurant(
            @Valid @RequestBody RestaurantCreateRequestDto requestDto
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.createRestaurant(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> updateRestaurant(
            @PathVariable Long id,
            @RequestBody RestaurantUpdateRequestDto requestDto
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.updateRestaurant(id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> findRestaurant(@PathVariable Long id){
        RestaurantDetailResponseDto responseDto = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantListResponseDto>> findRestaurants(){
        List<RestaurantListResponseDto> dtos = restaurantService.findAllRestaurants();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> closeRestaurant(@PathVariable Long id){
        restaurantService.closeRestaurant(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
