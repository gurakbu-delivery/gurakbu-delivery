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
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> updateRestaurant(
            @PathVariable Long id, @RequestBody RestaurantUpdateRequestDto requestDto
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.updateRestaurant(id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 가게 단건 조회
     *
     * @param id
     * @return RestaurantDetailResponseDto, Status 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> findRestaurant(
            @PathVariable Long id
    ){
        RestaurantDetailResponseDto responseDto = restaurantService.findRestaurantById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 가게 목록 조회
     * -> 페이징 처리 해야 함(공통 페이징 처리 필요)
     *
     * @return List<RestaurantListDto>, Status 200
     */
    @GetMapping
    public ResponseEntity<List<RestaurantListResponseDto>> findRestaurants(){
        List<RestaurantListResponseDto> dtos = restaurantService.findAllRestaurants();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * 가게 폐업
     * -> 인증/인가 구현되면 OWNER가 자신의 가게만 폐업할수 있도록 수정
     *
     * @param id
     * @return Status 200
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> closeRestaurant(@PathVariable Long id){
        restaurantService.closeRestaurant(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
