package com.gurakbu.delivery.domain.menu.controller;

import com.gurakbu.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.response.MenuResponseDto;
import com.gurakbu.delivery.domain.menu.service.MenuService;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final UserRepository userRepository;

    /**
     * 메뉴 생성
     * -> 해당 가게의 사장님만 요청 가능하도록 추후 권한 체크
     */
    @PostMapping("/{restaurantId}")
    public ResponseEntity<MenuResponseDto> createMenu(
            @AuthenticationPrincipal User user,
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuCreateRequestDto requestDto
    ){
        MenuResponseDto responseDto = menuService.createMenu(user, restaurantId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 메뉴 수정
     * -> 해당 가게의 사장님만 요청 가능하도록 추후 권한 체크
     */
    @PutMapping("/{restaurantId}/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @AuthenticationPrincipal User user,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto requestDto
    ){
        MenuResponseDto responseDto = menuService.updateMenu(user, restaurantId, menuId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 메뉴 삭제
     * -> 해당 가게의 사장님만 요청 가능하도록 추후 권한 체크
     */
    @DeleteMapping("/{restaurantId}/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @AuthenticationPrincipal User user,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId
    ){
        menuService.deleteMenu(user, restaurantId, menuId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}