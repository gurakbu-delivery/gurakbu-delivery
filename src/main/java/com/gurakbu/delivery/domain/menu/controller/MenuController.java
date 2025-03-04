package com.gurakbu.delivery.domain.menu.controller;

import com.gurakbu.delivery.domain.menu.dto.MenuRequestDto;
import com.gurakbu.delivery.domain.menu.dto.MenuResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor

public class MenuController {
    // 메뉴 생성
    @PostMapping("/{restaurentId}")
    public ResponseEntity<MenuResponseDto> createMenu(@PathVariable Long restaurentId,
                                                      @RequestBody MenuRequestDto menuRequestDto,
                                                      ) // @Auth AuthUser authUser 아직 인증 안 해둠
    {

    }
}
