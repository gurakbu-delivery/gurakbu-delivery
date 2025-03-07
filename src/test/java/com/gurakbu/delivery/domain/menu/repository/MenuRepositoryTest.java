package com.gurakbu.delivery.domain.menu.repository;

import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.entity.MenuCategory;
import com.gurakbu.delivery.domain.menu.entity.MenuStatus;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantCategory;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.enums.UserRole;
import com.gurakbu.delivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.text.html.Option;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private Menu menu1;
    private Menu menu2;
    private User user1;

    @BeforeEach
    void setUp() {
        // 1. 테스트용 유저 생성
        user1 = userRepository.save(new User("asdf@naver.com", "qlalfqjsgh1!","김미미", "01011111111", UserRole.ADMIN));

        // 2. 테스트용 가게 저장
        restaurant1 = new Restaurant("야채구락부", "스파르타읍 내배캠면 스프링리", "온 가족이 먹는 음식", RestaurantCategory.KOREAN, RestaurantStatus.OPEN, LocalTime.of(9,0), LocalTime.of(22,0),5000);
        restaurant1.setUser(user1);
        restaurant1 = restaurantRepository.save(restaurant1); // 영속화

        restaurant2 = new Restaurant("고기구락부", "스파르타읍 내배캠면 스프링리", "온 가족이 먹는 음식", RestaurantCategory.KOREAN, RestaurantStatus.OPEN, LocalTime.of(9,0), LocalTime.of(22,0),5000);
        restaurant2.setUser(user1);
        restaurant2 = restaurantRepository.save(restaurant2); // 영속화



        // 3. 테스트용 메뉴 저장
        menu1 = Menu.builder()
                .restaurant(restaurant1)
                .name("장충동왕왕족발보쌈")
                .price(30000)
                .category(MenuCategory.MAIN)
                .popularity(Boolean.FALSE)
                .status(MenuStatus.OPEN)
                .build();
        menu1 = menuRepository.save(menu1);

        menu2 = Menu.builder()
                .restaurant(restaurant1)
                .name("막국수")
                .price(10000)
                .category(MenuCategory.SIDE)
                .popularity(Boolean.TRUE)
                .status(MenuStatus.OPEN)
                .build();
        menu2 = menuRepository.save(menu2);
    }

    @Test
    void 레스토랑id와_메뉴id로_특정_메뉴를_조회할_수_있다(){
        // given

        // when
        System.out.println(restaurant1.getId()+", "+ menu1.getId());
        Optional<Menu> foundMenu = menuRepository.findByRestaurantIdAndId(restaurant1.getId(), menu1.getId());

        // then
        assertThat(foundMenu).isPresent();
        assertEquals(menu1.getId(), foundMenu.get().getId());
        assertEquals(menu1.getName(), foundMenu.get().getName());
        assertEquals(menu1.getPrice(), foundMenu.get().getPrice());
        assertEquals(menu1.getCategory(), foundMenu.get().getCategory());
        assertEquals(menu1.getPopularity(), foundMenu.get().getPopularity());
        assertEquals(menu1.getStatus(), foundMenu.get().getStatus());
        assertEquals(menu1.getCreatedAt(), foundMenu.get().getCreatedAt());
        assertEquals(menu1.getUpdatedAt(), foundMenu.get().getUpdatedAt());
    }

    @Test
    void 레스토랑이_존재하지_않아_특정_메뉴를_조회할_수_없다(){
        // given

        // when
        Optional<Menu> foundMenu = menuRepository.findByRestaurantIdAndId(999L, menu1.getId());

        // then
        assertThat(foundMenu).isEmpty();
    }

    @Test
    void 존재하지_않는_메뉴는_조회할_수_없다(){
        // given

        // when
        Optional<Menu> foundMenu = menuRepository.findByRestaurantIdAndId(restaurant1.getId(), 999L);

        // then
        assertThat(foundMenu).isEmpty();
    }

    @Test
    void 특정_가게의_전체_메뉴를_조회한다(){
        // given

        // when
        Optional<List<Menu>> foundMenus = menuRepository.findByRestaurantId(restaurant1.getId());

        // then
        assertThat(foundMenus).isPresent();                 // 메뉴 리스트의 존재 확인
        assertThat(foundMenus.get()).hasSize(2);   // 저장된 메뉴의 개수(2개) 확인
    }

    @Test
    void 특정_가게의_메뉴가_없는데_조회할_때는_빈_리스트를_반환한다(){
        // given

        // when
        Optional<List<Menu>> foundMenus = menuRepository.findByRestaurantId(restaurant2.getId());

        // then
        assertThat(foundMenus).isPresent();                 // 메뉴 리스트의 존재 확인
        assertThat(foundMenus.get()).isEmpty();             // 빈 리스트인지 확인
    }
}