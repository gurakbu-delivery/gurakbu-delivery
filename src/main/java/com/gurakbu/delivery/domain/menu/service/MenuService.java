package com.gurakbu.delivery.domain.menu.service;

import com.gurakbu.delivery.domain.menu.dto.MenuRequestDto;
import com.gurakbu.delivery.domain.menu.dto.MenuResponseDto;
import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@Transactional              // 클래스 전체에 적용 (모든 public 메서드에 트랜잭션 적용)
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    // 다른 레포지토리도 합쳐야 함
    /*
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final UserService userService;
     */

    // 메뉴 생성
    // public MenuResponseDto createMenu(Long restaurantId, MenuRequestDto requestDto, AuthUser authUser) {
    public MenuResponseDto createMenu(MenuRequestDto menuRequestDto) {
        // restaurantId 존재 여부 확인
//        Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                .orElseThrow(() -> new EntityNotFoundException("[!] 선택한 가게가 존재하지 않습니다."));

        // 사용자 존재 여부 확인
//        User user = userRepository.findById(authUser.getId())
//                .orElseThrow(() -> new EntityNotFoundException("[!] 사장님이 존재하지 않습니다."));

        // 사장님 권한 확인
//        if (!shop.getOwner().getId().equals(authUser.getId())) {
//            throw new AccessDeniedException("[!] 권한이 없습니다. 해당 가게의 사장님만 메뉴를 생성할 수 있습니다.");
//        }


        Menu menu = Menu.create(restaurant, menuRequestDto.getName(), menuRequestDto.getPrice(), menuRequestDto.getCategory(), menuRequestDto.getDescription(), menuRequestDto.getStatus(), menuRequestDto.getPopularity());
        Menu savedMenu = menuRepository.save(menu);

        return new MenuResponseDto(
                savedMenu.getId(),
                savedMenu.getName(),
                savedMenu.getPrice(),
                savedMenu.getCategory(),
                savedMenu.getDescription(),
                savedMenu.getPopularity(),
                savedMenu.getStatus(),
                savedMenu.getCreatedAt(),
                savedMenu.getUpdatedAt()
        );
    }

    // 메뉴 수정
    public MenuResponseDto updateMenu(Long restorantId, Long menuId, MenuRequestDto menuRequestDto, AuthUser authUser) {
        // restaurantId 존재 여부 확인
//        Restaurant restaurant = restaurantRepository.findById(restaurantId)
//                .orElseThrow(() -> new EntityNotFoundException("[!] 선택한 가게가 존재하지 않습니다."));

        // 사장님 권한 확인
//        if (!shop.getOwner().getId().equals(authUser.getId())) {
//            throw new AccessDeniedException("[!] 권한이 없습니다. 해당 가게의 사장님만 메뉴를 할 수 있습니다.");
//        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메뉴가 존재하지 않습니다."));

        if (!menu.getRestaurant().getId().equals(RestaurantId)) {
            throw new RuntimeException("해당 가게의 메뉴가 아닙니다.");
        }
    }

}
