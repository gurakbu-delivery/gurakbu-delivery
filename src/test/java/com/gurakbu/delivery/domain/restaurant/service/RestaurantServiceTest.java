package com.gurakbu.delivery.domain.restaurant.service;

import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantCreateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.request.RestaurantUpdateRequestDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.gurakbu.delivery.domain.restaurant.dto.response.RestaurantListResponseDto;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantCategory;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import com.gurakbu.delivery.domain.user.entity.User;
import com.gurakbu.delivery.domain.user.enums.UserRole;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private User owner;
    private User owner2;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        owner = new User("yumi@naver.com", "password1@", "유미사장", "01012345678", UserRole.OWNER);
        owner.setId(1L);
        owner2 = new User("heni@naver.com", "password1!", "헨사장", "01023456789", UserRole.OWNER);
        owner2.setId(2L);

        restaurant = new Restaurant(
                "유미네",
                "유미시 유미동",
                "고양이가 요리하는 가게...",
                RestaurantCategory.KOREAN,
                RestaurantStatus.OPEN,
                LocalTime.of(17, 0),
                LocalTime.of(22, 0),
                10000
        );
        restaurant.setId(1L);
        restaurant.setUser(owner);
    }

    @Test
    void 가게_생성에_성공한다() {
        // given
        RestaurantCreateRequestDto requestDto = new RestaurantCreateRequestDto(
                "유미네", "유미시 유미동", "고양이가 요리하는 가게...", RestaurantCategory.KOREAN,
                RestaurantStatus.OPEN, LocalTime.of(17, 0), LocalTime.of(22, 0), 10000
        );
        when(restaurantRepository.countByUserId(owner.getId())).thenReturn(0L);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // when
        RestaurantDetailResponseDto response = restaurantService.createRestaurant(owner, requestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(requestDto.getName());
        assertThat(response.getAddress()).isEqualTo(requestDto.getAddress());
        assertThat(response.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(response.getCategory()).isEqualTo(requestDto.getCategory());
        assertThat(response.getStatus()).isEqualTo(requestDto.getStatus());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void 가게_3개_초과_생성시_IAE_에러를_던진다() {
        // given
        RestaurantCreateRequestDto requestDto = new RestaurantCreateRequestDto(
                "유미네", "유미시 유미동", "고양이가 요리하는 가게...", RestaurantCategory.KOREAN,
                RestaurantStatus.OPEN, LocalTime.of(17, 0), LocalTime.of(22, 0), 10000
        );
        when(restaurantRepository.countByUserId(owner.getId())).thenReturn(3L);

        // when & then
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> restaurantService.createRestaurant(owner, requestDto));
        assertThat(exception.getMessage()).isEqualTo("한 사장님은 최대 3개의 가게만 생성할 수 있습니다.");
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void 가게를_정상적으로_수정한다() {
        // given
        RestaurantUpdateRequestDto requestDto = new RestaurantUpdateRequestDto(
                "수정된 가게", "수정된 주소", "수정된 설명", RestaurantCategory.CHINESE,
                LocalTime.of(18,0), LocalTime.of(21,0), 5000
        );
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // when
        RestaurantDetailResponseDto response = restaurantService.updateRestaurant(owner, restaurant.getId(), requestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(requestDto.getName());
        assertThat(response.getDescription()).isEqualTo(requestDto.getDescription());
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
    }

    @Test
    void 다른_사장이_가게_수정시_RSE_에러를_던진다() {
        // given
        RestaurantUpdateRequestDto requestDto = new RestaurantUpdateRequestDto(
                "수정된 가게", "수정된 주소", "수정된 설명", RestaurantCategory.CHINESE,
                LocalTime.of(18,0), LocalTime.of(21,0), 5000
        );
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // when & then
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> restaurantService.updateRestaurant(owner2, restaurant.getId(), requestDto));
        assertThat(exception.getMessage()).contains("수정 권한이 없습니다.");
    }

    @Test
    void 가게를_정상적으로_조회한다() {
        // given
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // when
        RestaurantDetailResponseDto response = restaurantService.findRestaurantById(restaurant.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(restaurant.getName());
        assertThat(response.getStatus()).isEqualTo(restaurant.getStatus());
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
    }

    @Test
    void 가게_목록을_정상적으로_조회한다() {
        // given
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        // when
        List<RestaurantListResponseDto> response = restaurantService.findAllRestaurants();

        // then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getName()).isEqualTo(restaurant.getName());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void 폐업된_가게는_목록_조회에서_제외된다() {
        // given
        Restaurant closedRestaurant = new Restaurant(
                "폐업가게", "어딘가", "폐업함", RestaurantCategory.KOREAN,
                RestaurantStatus.CLOSED, LocalTime.of(9, 0), LocalTime.of(22, 0), 5000
        );
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant, closedRestaurant));

        // when
        List<RestaurantListResponseDto> response = restaurantService.findAllRestaurants();

        // then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getName()).isEqualTo(restaurant.getName());
    }

    @Test
    void 가게를_정상적으로_폐업한다() {
        // given
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // when
        restaurantService.closeRestaurant(owner, restaurant.getId());

        // then
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        assertThat(restaurant.getStatus()).isEqualTo(RestaurantStatus.CLOSED);
    }

    @Test
    void 다른_사장이_가게_폐업시_RSE_에러를_던진다() {
        // given
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // when & then
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> restaurantService.closeRestaurant(owner2, restaurant.getId()));
        assertThat(exception.getMessage()).contains("폐업 권한이 없습니다.");
    }
}
