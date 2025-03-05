package com.gurakbu.delivery.domain.menu.controller;

import com.gurakbu.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.response.MenuResponseDto;
import com.gurakbu.delivery.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 생성 요청
     * -> 해당 가게의 사장님만 요청 가능하도록 수정해야 함
     *
     * @param menuCreateRequestDto
     * @return MenuResponseDto, 201 CREATED, 403 FORBIDDEN
     */

    // 메뉴 생성
    @PostMapping("/{restaurentId}")
    public ResponseEntity<MenuResponseDto> createMenu(
            @PathVariable Long restaurentId,
            @Valid @RequestBody MenuCreateRequestDto requestDto)
    {
        MenuResponseDto responseDto = menuService.createMenu(restaurentId, requestDto);   // 마지막 파라미터로 사용자인증 수정필요
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 메뉴 정보 수정 요청
     * -> 해당 가게의 사장님만 요청 가능
     *
     * @param restaurantId
     * @param menuId
     * @param menuUpdateRequestDto
     * @return MenuResponseDto, Status 200 OK, 403 FORBIDDEN
     */
    @PutMapping("/{restaurentId}/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long restaurentId,
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequestDto requestDto)
    {
        MenuResponseDto responseDto = menuService.updateMenu(restaurentId, menuId, requestDto);   // 마지막 파라미터로 사용자인증 수정필요
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 메뉴 삭제
     * -> 해당 가게의 사장님만 요청 가능
     *
     * @param restaurantId
     * @param menuId
     * @return Status 200 OK, 403 FORBIDDEN
     */
    @DeleteMapping("/{restaurantId}/{menuId}")
    public ResponseEntity<MenuResponseDto> deleteMenu(@PathVariable Long restaurentId, @PathVariable Long menuId){
        menuService.deleteMenu(restaurentId, menuId); // 사용자 인증필요
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
