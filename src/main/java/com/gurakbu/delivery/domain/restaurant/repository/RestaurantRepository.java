package com.gurakbu.delivery.domain.restaurant.repository;

import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    Long countByUserId(Long userId);
}
