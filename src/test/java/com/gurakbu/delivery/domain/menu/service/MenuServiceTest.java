package com.gurakbu.delivery.domain.menu.service;

import com.gurakbu.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.gurakbu.delivery.domain.menu.dto.response.MenuResponseDto;
import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.entity.MenuCategory;
import com.gurakbu.delivery.domain.menu.entity.MenuStatus;
import com.gurakbu.delivery.domain.menu.repository.MenuRepository;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantCategory;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks    // 실제 테스트할 서비스
    private MenuService menuService;
    private Restaurant myRestaurant;
    private Restaurant elseRestaurant;
    private Menu myMenu;
    private Menu elseMenu;

    @BeforeEach
    void setUp() {
        myRestaurant = new Restaurant("야채구락부", "스파르타읍 내배캠면 스프링리", "온 가족이 먹는 음식", RestaurantCategory.KOREAN, RestaurantStatus.OPEN, LocalTime.of(9,0), LocalTime.of(22,0), 5000);
        elseRestaurant = new Restaurant("고기구락부", "스파르타읍 내배캠면 스프링리", "온 가족이 먹는 음식", RestaurantCategory.KOREAN, RestaurantStatus.OPEN, LocalTime.of(9,0), LocalTime.of(22,0), 5000);
        myMenu = Menu.builder()
                .id(1L)
                .restaurentId(myRestaurant.getId())
                .name("장충동왕왕족발보쌈")
                .price(30000)
                .category(MenuCategory.SIDE)
                .popularity(false)
                .status(MenuStatus.OPEN)
                .build();
        elseMenu = Menu.builder()
                .id(2L)
                .restaurentId(elseRestaurant.getId())
                .name("돼지갈비&냉면SET")
                .price(10000)
                .category(MenuCategory.MAIN)
                .popularity(true)
                .status(MenuStatus.OPEN)
                .build();
    }

    @Test
    @DisplayName("메뉴 생성 - 성공 케이스")
    void menu가_정상적으로_생성된다() {
        // given (준비) : 생성 요청 & 메뉴 객체 & 레포지토리 동작
        Long restaurantId = myRestaurant.getId();
        MenuCreateRequestDto requestDto = new MenuCreateRequestDto(
                "막국수", 5000, MenuCategory.SIDE, "족발먹는데 막국수를 안 드시려구요?", false, MenuStatus.OPEN
        );
        Menu menu = Menu.create(restaurantId, requestDto.getName(), requestDto.getPrice(), requestDto.getCategory(), requestDto.getDescription(), requestDto.getStatus(), requestDto.getPopularity());
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(myRestaurant));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        // when (실행)
        MenuResponseDto createdMenu = menuService.createMenu(myRestaurant.getId(), requestDto);

        // then (검증)
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getName()).isEqualTo(requestDto.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(createdMenu.getCategory()).isEqualTo(requestDto.getCategory());
        assertThat(createdMenu.getStatus()).isEqualTo(requestDto.getStatus());
        assertThat(createdMenu.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(createdMenu.getPopularity()).isEqualTo(requestDto.getPopularity());

        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    @DisplayName("메뉴 생성 - 예외 케이스: 존재하지 않는 restaurantId 예외")
    void menu_생성_시_restaurantId가_조회되지_않는_경우_IAE_에러를_던진다(){
        // given (준비)
        // when (실행)
        // then (검증)
    }

    @Test
    @DisplayName("메뉴 생성 - 예외 케이스: 존재하지 않는 userId 예외")
    void menu_생성_시_userId가_조회되지_않는_경우_IAE_에러를_던진다(){
        // given (준비)
        // when (실행)
        // then (검증)
    }

    @Test
    @DisplayName("메뉴 생성 - 예외 케이스: 메뉴가 해당 가게에 속하지 않는 예외")
    void menu_생성_시_해당_가게의_userId로_접근하지_않은_경우_ADE_에러를_던진다(){
        // given (준비)
        // when (실행)
        // then (검증)
    }

    @Test
    @DisplayName("메뉴 수정 - 성공 케이스")
    void menu가_정상적으로_수정된다() {
        // given (준비) : 수정 요청 & 메뉴 객체 & 레포지토리 동작
        Long restaurantId = myRestaurant.getId();
        Long menuId = myMenu.getId();
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto(null, null, MenuCategory.MAIN, null, true, null);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(myRestaurant));
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(myMenu));

        // when (실행)
        MenuResponseDto updatedMenu = menuService.updateMenu(myRestaurant.getId(), menuId, requestDto);

        // then (검증)
        assertThat(updatedMenu).isNotNull();
        assertThat(updatedMenu.getName()).isEqualTo(requestDto.getName());
        assertThat(updatedMenu.getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(updatedMenu.getCategory()).isEqualTo(requestDto.getCategory());
        assertThat(updatedMenu.getStatus()).isEqualTo(requestDto.getStatus());
        assertThat(updatedMenu.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(updatedMenu.getPopularity()).isEqualTo(requestDto.getPopularity());

        assertThat(updatedMenu.getName()).isEqualTo(myMenu.getName());
        assertThat(updatedMenu.getCategory()).isEqualTo(myMenu.getCategory());

        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void menu_수정_시_restaurantId가_조회되지_않는_경우_RSE_NOT_FOUND_에러를_던진다(){
        // given (준비)
        Long invalidRestaurantId = elseRestaurant.getId();    // 실제로는 1L임
        Long menuId = myMenu.getId();
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto(null, 25000, MenuCategory.MAIN, null, true, null);

        when(restaurantRepository.findById(invalidRestaurantId)).thenReturn(Optional.empty());  // 잘못된 restaurantId

        // when (실행) & then (검증)
        assertThrows(ResponseStatusException.class, () -> menuService.updateMenu(invalidRestaurantId, menuId, requestDto));
    }

    @Test
    void menu_수정_시_userId가_조회되지_않는_경우_IAE_에러를_던진다(){
        // given (준비)
        // when (실행)
        // then (검증)
    }

    @Test
    void menu_수정_시_menuId가_조회되지_않는_경우_RSE_NOT_FOUND_에러를_던진다(){
        // given (준비)
        Long restaurantId = myRestaurant.getId();
        Long invalidMenuId = elseMenu.getId();
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto(null, 25000, MenuCategory.MAIN, null, true, null);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(myRestaurant));
        when(menuRepository.findById(invalidMenuId)).thenReturn(Optional.empty());

        // when (실행) & then (검증)
        assertThrows(ResponseStatusException.class, () -> menuService.updateMenu(restaurantId, invalidMenuId, requestDto));
    }

    @Test
    void menu_수정_시_해당_가게의_userId로_접근하지_않은_경우_RE_에러를_던진다(){
        // given (준비)
        // when (실행)
        // then (검증)
    }
}
