package com.gurakbu.delivery.domain.menu.service;

import com.gurakbu.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.response.MenuResponseDto;
import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.repository.MenuRepository;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.repository.UserRepository;
import com.gurakbu.delivery.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@Transactional           // 모든 public class에 적용
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // 메뉴 생성
    public MenuResponseDto createMenu(User authUser, Long restaurantId, MenuCreateRequestDto requestDto) {
         // restaurantId 존재 여부 확인
         Restaurant restaurant = restaurantRepository.findById(restaurantId)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));   // HTTP 404 Not Found

         // userId 존재 여부 확인
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자"));   // HTTP 404 Not Found

         // user 권한 확인 - restaurant.getUserId만 가능해지면 오류 없어짐
        if (!restaurant.getUserId().equals(authUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한없음: 해당 가게의 사장님이 아닙니다");      // HTTP 403 Forbidden
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

    // 메뉴 수정
    public MenuResponseDto updateMenu(User authUser, Long restaurantId, Long menuId, MenuUpdateRequestDto requestDto) {
        // restaurantId 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));    // HTTP 404 Not Found

        // user 권한 확인 - restaurant.getUserId만 가능해지면 오류 없어짐
        if (!restaurant.getUserId().equals(authUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한없음: 해당 가게의 사장님이 아닙니다");      // HTTP 403 Forbidden
        }

        // MenuId 존재 여부 확인
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴"));    // HTTP 404 Not Found

        // 메뉴가 연결된 레스토랑이 요청한 레스토랑과 같은지 권한 확인
        if (!menu.getRestaurant().getId().equals(restaurant.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한없음: 해당 가게의 메뉴가 아닙니다");
        }

        menu.update(requestDto);
        return MenuResponseDto.fromEntity(menu);
    }

    // 메뉴 삭제
    public void deleteMenu(User authUser, Long restaurantId, Long menuId) {
        // restaurantId 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게"));    // HTTP 404 Not Found

        // user가 restaurant의 OWNER인지 권한 확인 - restaurant.getUserId만 가능해지면 오류 없어짐
        if (!restaurant.getUserId().equals(authUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한없음: 해당 가게의 사장님이 아닙니다");      // HTTP 403 Forbidden
        }

        // MenuId 존재 여부 확인
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴"));    // HTTP 404 Not Found

        // 메뉴가 연결된 레스토랑이 요청한 레스토랑과 같은지 권한 확인
        if (!menu.getRestaurant().getId().equals(restaurant.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한없음: 해당 가게의 메뉴가 아닙니다");
        }
        menu.delete();
    }
}