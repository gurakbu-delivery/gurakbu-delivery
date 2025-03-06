package com.gurakbu.delivery.domain.menu.service;

import com.gurakbu.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.response.MenuResponseDto;
import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.repository.MenuRepository;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    // 메뉴 생성: 요청한 사용자가 해당 가게의 OWNER 혹은 ADMIN이어야 함
    public MenuResponseDto createMenu(Long restaurantId, MenuCreateRequestDto requestDto, User user) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("[!] 선택한 가게가 존재하지 않습니다."));

        // 권한 체크: 관리자(ADMIN)이거나, 사용자가 사장님(OWNER) 역할이어야 함.
        if (!user.isAdmin() && !user.isOwner(restaurantId)) {
            throw new IllegalStateException("[!] 해당 가게의 사장님만 메뉴를 생성할 수 있습니다.");
        }

        Menu menu = Menu.create(
                restaurant,
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getCategory(),
                requestDto.getDescription(),
                requestDto.getStatus(),
                requestDto.getPopularity()
        );

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponseDto.fromEntity(savedMenu);
    }

    // 메뉴 수정: 요청한 사용자가 해당 가게의 OWNER 혹은 ADMIN이어야 함
    public MenuResponseDto updateMenu(Long restaurantId, Long menuId, MenuUpdateRequestDto requestDto, User user) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 가게"));

        if (!user.isAdmin() && !user.isOwner(restaurantId)) {
            throw new IllegalStateException("[!] 해당 가게의 사장님만 메뉴를 수정할 수 있습니다.");
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 메뉴"));

        if (!menu.getRestaurant().getId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "[!] 수정하려는 메뉴가 해당 가게의 메뉴가 아닙니다.");
        }
        menu.update(requestDto);
        return MenuResponseDto.fromEntity(menu);
    }

    // 메뉴 삭제: 요청한 사용자가 해당 가게의 OWNER 혹은 ADMIN이어야 함
    public void deleteMenu(Long restaurantId, Long menuId, User user) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 가게"));

        if (!user.isAdmin() && !user.isOwner(restaurantId)) {
            throw new IllegalStateException("[!] 해당 가게의 사장님만 메뉴를 삭제할 수 있습니다.");
        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 메뉴"));

        if (!menu.getRestaurant().getId().equals(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "[!] 삭제하려는 메뉴가 해당 가게의 메뉴가 아닙니다.");
        }
        menu.delete();
    }

    // 메뉴 조회: 해당 가게의 모든 메뉴 조회
    @Transactional
    public List<MenuResponseDto> getMenusByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 가게"));

        List<Menu> menus = menuRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));

        return menus.stream()
                .map(MenuResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
