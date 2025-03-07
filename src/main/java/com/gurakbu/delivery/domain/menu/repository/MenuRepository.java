package com.gurakbu.delivery.domain.menu.repository;

import com.gurakbu.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    // 특정 레스토랑에 해당 메뉴가 존재하는지 확인하는 메서드: Optional<Menu> 반환 (존재하지 않으면 Optional.empty())
     Optional<Menu> findByRestaurantIdAndId(Long restaurantId, Long id);

     Optional<List<Menu>> findByRestaurantId(Long restaurantId);
}