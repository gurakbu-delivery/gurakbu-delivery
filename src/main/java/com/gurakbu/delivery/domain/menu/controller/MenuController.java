package com.gurakbu.delivery.domain.menu.controller;

import com.gurakbu.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.response.MenuResponseDto;
import com.gurakbu.delivery.domain.menu.service.MenuService;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.service.UserService;
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
    private final UserService userService;

    @PostMapping("/{restaurantId}")
    public ResponseEntity<MenuResponseDto> createMenu(
            @AuthenticationPrincipal String email, // String으로 받기
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuCreateRequestDto requestDto
    ){
        // 컨트롤러에서 User 엔티티 직접 조회
        User user = userService.findByEmail(email);

        MenuResponseDto responseDto = menuService.createMenu(restaurantId, requestDto, user);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{restaurantId}/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @AuthenticationPrincipal String email,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto requestDto
    ){
        User user = userService.findByEmail(email);

        MenuResponseDto responseDto = menuService.updateMenu(restaurantId, menuId, requestDto, user);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{restaurantId}/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @AuthenticationPrincipal String email,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId
    ){
        User user = userService.findByEmail(email);

        menuService.deleteMenu(restaurantId, menuId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}