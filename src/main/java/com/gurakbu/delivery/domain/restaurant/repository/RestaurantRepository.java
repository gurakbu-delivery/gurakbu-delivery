package com.gurakbu.delivery.domain.restaurant.repository;

import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
}
