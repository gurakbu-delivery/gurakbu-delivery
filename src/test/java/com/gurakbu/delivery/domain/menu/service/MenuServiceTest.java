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
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.enums.UserRole;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuService menuService;

    private Restaurant myRestaurant;
    private Restaurant yourRestaurant;
    private User owner;
    private User yourOwner;
    private User admin;
    private User yourAdmin;
    private User customer;
    private Menu myMenu;
    private Menu yourMenu;

    @BeforeEach
    void setUp() {
        // 사용자 생성
        owner = new User("abc1234@naver.com", "qlalfqjsgh1!", "김미미", "01011111111", UserRole.OWNER);
        yourOwner = new User("asdf1234@naver.com", "qlalfqjsgh1!", "이미미", "01011111111", UserRole.OWNER);
        admin = new User("bcd1234@naver.com", "qlalfqjsgh1!", "김먀먀", "01011111111", UserRole.ADMIN);
        yourAdmin = new User("bcdf1234@naver.com", "qlalfqjsgh1!", "이먀먀", "01011111111", UserRole.ADMIN);
        customer = new User("efg1234@naver.com", "qlalfqjsgh1!", "김묘묘", "01011111111", UserRole.USER);

        // 가게 생성
        myRestaurant = new Restaurant("야채구락부", "스파르타읍 내배캠면 스프링리", "온 가족이 먹는 음식", RestaurantCategory.KOREAN, RestaurantStatus.OPEN, LocalTime.of(9, 0), LocalTime.of(22, 0), 5000);
        myRestaurant.setId(1L);
        yourRestaurant = new Restaurant("고기구락부", "스파르타읍 내배캠면 스프링리", "고기냠냠", RestaurantCategory.KOREAN, RestaurantStatus.OPEN, LocalTime.of(9, 0), LocalTime.of(22, 0), 5000);
        yourRestaurant.setId(2L);

        // 메뉴 생성
        myMenu = Menu.builder()
                .id(1L)
                .restaurant(myRestaurant)
                .name("장충동왕왕족발보쌈")
                .price(30000)
                .category(MenuCategory.SIDE)
                .description("노래가 절로 나오는")
                .popularity(Boolean.FALSE)
                .status(MenuStatus.OPEN)
                .build();

        yourMenu = Menu.builder()
                .id(2L)
                .restaurant(yourRestaurant)
                .name("돼지갈비&냉면")
                .price(10000)
                .category(MenuCategory.MAIN)
                .description("맛있겠다")
                .popularity(Boolean.TRUE)
                .status(MenuStatus.OPEN)
                .build();
    }

    @Test
    void 사장님_요청으로_menu가_정상적으로_생성된다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuCreateRequestDto requestDto = new MenuCreateRequestDto("장충동왕왕족발보쌈", 30000, MenuCategory.SIDE, "노래가 절로 나오는", Boolean.FALSE, MenuStatus.OPEN);
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
        when(menuRepository.save(any(Menu.class))).thenReturn(myMenu);  // menuRepository.save()를 하는 경우 무조건 myMenu를 반환하도록 설정
        // when (실행)
        MenuResponseDto createdMenu = menuService.createMenu(myRestaurant.getId(), requestDto, owner);
        System.out.println("myRestaurant ID, status " + myRestaurant.getId() + createdMenu.getStatus());

        // then (검증) : 요청DTO와 응답DTO비교
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getName()).isEqualTo(requestDto.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(createdMenu.getCategory()).isEqualTo(requestDto.getCategory());
        assertThat(createdMenu.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(createdMenu.getPopularity()).isEqualTo(requestDto.getPopularity());
        assertThat(createdMenu.getStatus()).isEqualTo(requestDto.getStatus());
        assertThat(createdMenu.getCategory()).isEqualTo(requestDto.getCategory());

        verify(menuRepository, times(1)).save(any(Menu.class)); // 해당 과정에 있어서 menuRepository.save()가 반드시 한 번 실행되었는지 확인
    }

    @Test
    void 관리자_요청으로_menu가_정상적으로_생성된다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuCreateRequestDto requestDto = new MenuCreateRequestDto("장충동왕왕족발보쌈", 30000, MenuCategory.SIDE, "노래가 절로 나오는", Boolean.FALSE, MenuStatus.OPEN);
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
        when(menuRepository.save(any(Menu.class))).thenReturn(myMenu);  // menuRepository.save()를 하는 경우 무조건 myMenu를 반환하도록 설정
        // when (실행)
        MenuResponseDto createdMenu = menuService.createMenu(myRestaurant.getId(), requestDto, admin);

        // then (검증) : 요청DTO와 응답DTO비교
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getName()).isEqualTo(requestDto.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(createdMenu.getCategory()).isEqualTo(requestDto.getCategory());
        assertThat(createdMenu.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(createdMenu.getPopularity()).isEqualTo(requestDto.getPopularity());
        assertThat(createdMenu.getStatus()).isEqualTo(requestDto.getStatus());
        assertThat(createdMenu.getCategory()).isEqualTo(requestDto.getCategory());

        verify(menuRepository, times(1)).save(any(Menu.class)); // 해당 과정에 있어서 menuRepository.save()가 반드시 한 번 실행되었는지 확인
    }

    @Test
    void 가게조회가_안_되는_상태로_menu_생성시_IAE_에러를_던진다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuCreateRequestDto requestDto = new MenuCreateRequestDto("장충동왕왕족발보쌈", 30000, MenuCategory.SIDE, "노래가 절로 나오는", Boolean.FALSE, MenuStatus.OPEN);
        Long invalidRestaurantId = 10L;
        when(restaurantRepository.findById(invalidRestaurantId)).thenReturn(Optional.empty());    // restaurantRepository.findById(유효하지 않은 ID)를 사용하는 경우, 빈 결과를 반환하도록 설정

        // when (실행) & then (검증)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> menuService.createMenu(invalidRestaurantId, requestDto, owner));
        assertThat(exception.getMessage()).contains("[!] 선택한 가게가 존재하지 않습니다."); // 에러메세지확인

        verify(restaurantRepository, times(1)).findById(invalidRestaurantId);   // 비정상적인 id를 조회하려는 작업이 있었는지 확인
        verify(menuRepository, never()).save(any(Menu.class));  // 레포지토리 작업이 일어나지 않았는지 확인
    }

    @Test
    void 일반_사용자로_menu_생성요청시_ISE_에러를_던진다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuCreateRequestDto requestDto = new MenuCreateRequestDto("장충동왕왕족발보쌈", 30000, MenuCategory.SIDE, "노래가 절로 나오는", Boolean.FALSE, MenuStatus.OPEN);
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정

        // when (실행) & then (검증)
        Exception exception = assertThrows(IllegalStateException.class, () -> menuService.createMenu(myRestaurant.getId(), requestDto, customer));
        assertThat(exception.getMessage()).contains("해당 가게의 사장님만 메뉴를 생성할 수 있습니다."); // 에러메세지확인
        verify(menuRepository, never()).save(any(Menu.class));  // 레포지토리 작업이 일어나지 않았는지 확인
    }

//    @Test
//    void 타_가게의_사장이_menu_생성요청시_ISE_에러를_던진다() {
//        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
//        MenuCreateRequestDto requestDto = new MenuCreateRequestDto("장충동왕왕족발보쌈", 30000, MenuCategory.SIDE, "노래가 절로 나오는", Boolean.FALSE, MenuStatus.OPEN);
//        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
//
//        // when (실행) & then (검증)
//        Exception exception = assertThrows(IllegalStateException.class, () -> menuService.createMenu(myRestaurant.getId(), requestDto, yourOwner));
//        assertThat(exception.getMessage()).contains("[!] 해당 가게의 사장님만 메뉴를 생성할 수 있습니다."); // 에러메세지확인
//        verify(menuRepository, never()).save(any(Menu.class));  // 레포지토리 작업이 일어나지 않았는지 확인
//    }

    @Test
    void 특정_가게ID를_통해_모든_메뉴를_조회한다(){
        // given (준비) : 레포지토리 동작만들기
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
        when(menuRepository.findByRestaurantId(myRestaurant.getId())).thenReturn(Optional.of(List.of(myMenu)));  // 위와 동일한 원리

        // when (실행)
        List<MenuResponseDto> menus = menuService.getMenusByRestaurant(myRestaurant.getId());

        // then (검증)
        assertThat(menus).hasSize(1);   // 메뉴 목록의 원소갯수가 1개인지 확인
        assertThat(menus.get(0).getName()).isEqualTo(myMenu.getName());
        assertThat(menus.get(0).getPrice()).isEqualTo(myMenu.getPrice());
        assertThat(menus.get(0).getCategory()).isEqualTo(myMenu.getCategory());
        assertThat(menus.get(0).getDescription()).isEqualTo(myMenu.getDescription());
        assertThat(menus.get(0).getPopularity()).isEqualTo(myMenu.getPopularity());
        assertThat(menus.get(0).getStatus()).isEqualTo(myMenu.getStatus());
        verify(menuRepository, times(1)).findByRestaurantId(myRestaurant.getId());  // 해당 레포지토리 메소드를 1회 사용하였는지 확인
    }

    @Test
    void 존재하지_않는_가게_조회시_RSE_에러를_던진다() {
        // given (준비) : 레포지토리 동작만들기
        Long invalidRestaurantId = 10L;
        when(restaurantRepository.findById(invalidRestaurantId)).thenReturn(Optional.empty());

        // when (실행) & then (검증)
        assertThatThrownBy(() -> menuService.getMenusByRestaurant(invalidRestaurantId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("[!] 존재하지 않는 가게");

        verify(menuRepository, never()).findByRestaurantId(anyLong()); // 메뉴 조회가 수행되지 않아야 함
    }

    @Test
    void 가게에_메뉴가_없는데_조회시_IAE_에러를_던진다() {
        // given (준비) : 레포지토리 동작만들기
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));
        when(menuRepository.findByRestaurantId(myRestaurant.getId())).thenReturn(Optional.empty());

        // when (실행) & then (검증)
        assertThatThrownBy(() -> menuService.getMenusByRestaurant(myRestaurant.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴가 존재하지 않습니다.");

        verify(menuRepository, times(1)).findByRestaurantId(myRestaurant.getId()); // 메뉴 조회 요청은 수행되어야 한다
    }

    @Test
    void 사장님_요청으로_menu가_정상적으로_수정된다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto("장충동왕족발&막국수", 40000, null, "SET MENU", Boolean.TRUE, null);
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
        when(menuRepository.findById(myMenu.getId())).thenReturn(Optional.of(myMenu));  // 위와 동일한 원리

        // when (실행)
        MenuResponseDto updatedMenu = menuService.updateMenu(myRestaurant.getId(), myMenu.getId(), requestDto, owner);

        // then (검증) : 요청DTO와 응답DTO비교
        assertThat(updatedMenu).isNotNull();
        assertThat(updatedMenu.getName()).isEqualTo(requestDto.getName());
        assertThat(updatedMenu.getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(updatedMenu.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(updatedMenu.getPopularity()).isEqualTo(requestDto.getPopularity());

        verify(menuRepository, times(1)).findById(myMenu.getId());  // 해당 과정에 있어서 menu를 찾는 과정이 1회 있었는지 확인
    }

    @Test
    void 관리자_요청으로_menu가_정상적으로_수정된다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto("장충동왕족발&막국수", 40000, null, "SET MENU", Boolean.TRUE, null);
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
        when(menuRepository.findById(myMenu.getId())).thenReturn(Optional.of(myMenu));  // 위와 동일한 원리

        // when (실행)
        MenuResponseDto updatedMenu = menuService.updateMenu(myRestaurant.getId(), myMenu.getId(), requestDto, admin);

        // then (검증) : 요청DTO와 응답DTO비교
        assertThat(updatedMenu).isNotNull();
        assertThat(updatedMenu.getName()).isEqualTo(requestDto.getName());
        assertThat(updatedMenu.getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(updatedMenu.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(updatedMenu.getPopularity()).isEqualTo(requestDto.getPopularity());

        verify(menuRepository, times(1)).findById(myMenu.getId());  // 해당 과정에 있어서 menu를 찾는 과정이 1회 있었는지 확인
    }

    @Test
    void 일반_사용자로_menu수정요청시_ISE_에러를_던진다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto("장충동왕족발&막국수", 40000, null, "SET MENU", Boolean.TRUE, null);
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정

        // when (실행) & then (검증)
        Exception exception = assertThrows(IllegalStateException.class, () -> menuService.updateMenu(myRestaurant.getId(), myMenu.getId(), requestDto, customer));
        assertThat(exception.getMessage()).contains("해당 가게의 사장님만 메뉴를 수정할 수 있습니다."); // 에러메세지확인
        verify(menuRepository, never()).save(any(Menu.class));  // 레포지토리 작업이 일어나지 않았는지 확인
    }

    @Test
    void 가게조회가_안_되는_상태로_menu_수정요청시_RSE_에러를_던진다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto("장충동왕족발&막국수", 40000, null, "SET MENU", Boolean.TRUE, null);
        Long invalidRestaurantId = 10L;
        when(restaurantRepository.findById(invalidRestaurantId)).thenReturn(Optional.empty());    // restaurantRepository.findById(유효하지 않은 ID)를 사용하는 경우, 빈 결과를 반환하도록 설정

        // when (실행) & then (검증)
        Exception exception = assertThrows(ResponseStatusException.class, () -> menuService.updateMenu(invalidRestaurantId, myMenu.getId(), requestDto, customer));
        assertThat(exception.getMessage()).contains("[!] 존재하지 않는 가게"); // 에러메세지확인
    }

    @Test
    void 존재하지_않는_menu_수정요청시_RSE_에러를_던진다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        Long invalidMenuId = 10L;
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto("장충동왕족발&막국수", 40000, null, "SET MENU", Boolean.TRUE, null);
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(유효하지 않은 ID)를 사용하는 경우, 빈 결과를 반환하도록 설정
        when(menuRepository.findById(invalidMenuId)).thenReturn(Optional.empty());

        // when (실행) & then (검증)
        Exception exception = assertThrows(ResponseStatusException.class, () -> menuService.updateMenu(myRestaurant.getId(), invalidMenuId, requestDto, owner));
        assertThat(exception.getMessage()).contains("[!] 존재하지 않는 메뉴"); // 에러메세지확인
    }

    @Test
    void 타_가게의_사장이_menu_수정요청시_RSE_에러를_던진다() {
        // given (준비) : 요청DTO 생성 & 레포지토리 동작만들기
        MenuUpdateRequestDto requestDto = new MenuUpdateRequestDto("장충동왕족발&막국수", 40000, null, "SET MENU", Boolean.TRUE, null);
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
        when(menuRepository.findById(yourMenu.getId())).thenReturn(Optional.of(yourMenu));

        // when (실행) & then (검증)
        Exception exception = assertThrows(ResponseStatusException.class, () -> menuService.updateMenu(myRestaurant.getId(), yourMenu.getId(), requestDto, owner));
        assertThat(exception.getMessage()).contains("[!] 수정하려는 메뉴가 해당 가게의 메뉴가 아닙니다."); // 에러메세지확인
        verify(menuRepository, never()).save(any(Menu.class));  // 레포지토리 작업이 일어나지 않았는지 확인
    }

    @Test
    void 사장님_요청으로_menu가_정상적으로_삭제된다() {
        // given (준비) : 레포지토리 동작만들기
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
        when(menuRepository.findById(myMenu.getId())).thenReturn(Optional.of(myMenu));  // 위와 동일한 원리

        // when (실행)
        menuService.deleteMenu(myRestaurant.getId(), myMenu.getId(), owner);
        // then (검증)
        verify(menuRepository, times(1)).findById(myMenu.getId());
        verify(menuRepository, never()).save(any(Menu.class)); // 필드만 변경하는 삭제이므로 실제 삭제가 일어나지않았는지 제크
    }

    @Test
    void 관리자_요청으로_menu가_정상적으로_삭제된다() {
        // given (준비) : 레포지토리 동작만들기
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));    // restaurantRepository.findById(restaurantId)를 사용하는 경우, 실제 DB를 조회하지 않고 restaurant의 id를 반환하도록 설정
        when(menuRepository.findById(myMenu.getId())).thenReturn(Optional.of(myMenu));  // 위와 동일한 원리

        // when (실행)
        menuService.deleteMenu(myRestaurant.getId(), myMenu.getId(), admin);
        // then (검증)
        verify(menuRepository, times(1)).findById(myMenu.getId());
        verify(menuRepository, never()).save(any(Menu.class)); // 필드만 변경하는 삭제이므로 실제 삭제가 일어나지않았는지 제크
    }

    @Test
    void 존재하지_않는_가게를_삭제요청시_RSE_에러를_던진다() {
        // given (준비) : 레포지토리 동작만들기
        Long invalidRestaurantId = 10L;
        when(restaurantRepository.findById(invalidRestaurantId)).thenReturn(Optional.empty());

        // when (실행) & then (검증)
        assertThatThrownBy(() -> menuService.deleteMenu(invalidRestaurantId, myMenu.getId(), owner))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("[!] 존재하지 않는 가게");

        verify(menuRepository, never()).findById(anyLong()); // 메뉴 조회조차 수행되지 않아야 함
    }

    @Test
    void 일반_사용자가_삭제요청시_ISE_에러를_던진다() {
        // given (준비) : 레포지토리 동작만들기
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));

        // when (실행) & then (검증)
        assertThatThrownBy(() -> menuService.deleteMenu(myRestaurant.getId(), myMenu.getId(), customer))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("[!] 해당 가게의 사장님만 메뉴를 삭제할 수 있습니다.");

        verify(menuRepository, never()).findById(anyLong()); // 메뉴 조회조차 수행되지 않아야 함
    }

    @Test
    void 다른_가게의_메뉴를_삭제요청시_RSE_에러를_던진다() {
        // given (준비) : 레포지토리 동작만들기
        when(restaurantRepository.findById(myRestaurant.getId())).thenReturn(Optional.of(myRestaurant));
        when(menuRepository.findById(yourMenu.getId())).thenReturn(Optional.of(yourMenu));

        // when (실행) & then (검증)
        assertThatThrownBy(() -> menuService.deleteMenu(myRestaurant.getId(), yourMenu.getId(), owner))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("[!] 삭제하려는 메뉴가 해당 가게의 메뉴가 아닙니다.");

        verify(menuRepository, times(1)).findById(yourMenu.getId());// 메뉴 조회 1회 수행
    }
}