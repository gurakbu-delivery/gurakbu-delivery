package com.gurakbu.delivery.domain.menu.service;

import com.gurakbu.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.response.MenuResponseDto;
import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.repository.MenuRepository;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
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
    //private final UserRepository userRepository;
    //private final UserService userService;

    // 메뉴 생성
    public MenuResponseDto createMenu(Long restaurantId, MenuCreateRequestDto requestDto) { // 마지막 파라미터로 user인증필요
         // restaurantId 존재 여부 확인
         Restaurant restaurant = restaurantRepository.findById(restaurantId)
                 .orElseThrow(() -> new IllegalArgumentException("[!] 선택한 가게가 존재하지 않습니다."));

         // 사용자 존재 여부 확인
//        User user = userRepository.findById(authUser.getId())
//                .orElseThrow(() -> new IllegalArgumentException("[!] 사장님이 존재하지 않습니다."));

         // 사장님 권한 확인
//        if (!restaurant.getOwner().getId().equals(authUser.getId())) {
//            throw new AccessDeniedException("[!] 권한이 없습니다. 해당 가게의 사장님만 메뉴를 생성할 수 있습니다.");
//        }

         Menu menu = Menu.create(
                 restaurantId,
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
    public MenuResponseDto updateMenu(Long restaurantId, Long menuId, MenuUpdateRequestDto requestDto) { // 마지막 파라미터로 user인증필요
//         restaurantId 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 가게"));

        // 사장님 권한 확인
//        if (!shop.getOwner().getId().equals(authUser.getId())) {
//            throw new AccessDeniedException("[!] 권한이 없습니다. 해당 가게의 사장님만 메뉴를 수정할 수 있습니다.");
//        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 메뉴"));

        if (!menu.getRestaurentId().equals(restaurantId)) {
            throw new RuntimeException("[!] 수정하려는 메뉴가 사장님 가게의 메뉴가 아닙니다.");
        }
        menu.update(requestDto);
        return MenuResponseDto.fromEntity(menu);
    }

    // 메뉴 삭제
    public void deleteMenu(Long restaurantId, Long menuId) {            // 마지막 파라미터로 user인증필요
        //         restaurantId 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 가게"));

        // 사장님 권한 확인
//        if (!shop.getOwner().getId().equals(authUser.getId())) {
//            throw new AccessDeniedException("[!] 권한이 없습니다. 해당 가게의 사장님만 메뉴를 삭제할 수 있습니다.");
//        }

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "[!] 존재하지 않는 메뉴"));

        if (!menu.getRestaurent().getId().equals(restaurantId)) {
            throw new RuntimeException("[!] 삭제하려는 메뉴가 사장님 가게의 메뉴가 아닙니다.");
        }
        menu.delete();
    }
}