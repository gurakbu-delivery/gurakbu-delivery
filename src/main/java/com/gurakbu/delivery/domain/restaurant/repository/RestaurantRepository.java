package com.gurakbu.delivery.domain.restaurant.repository;

import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.enums.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    List<Restaurant> findAllByCategoryAndStatusNotOrderByUpdatedAtDesc(String category, RestaurantStatus status);

    long countByUserIdAndStatus(Long id, RestaurantStatus restaurantStatus);

    List<Restaurant> getRestaurantsByCategory(RestaurantStatus status);
}
