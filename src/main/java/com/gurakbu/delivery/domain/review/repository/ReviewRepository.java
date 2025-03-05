package com.gurakbu.delivery.domain.review.repository;

import com.gurakbu.delivery.domain.order.entity.Order;
import com.gurakbu.delivery.domain.review.entity.Review;
import com.gurakbu.delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAll();

    List<Review> findByUserId(Long userId);

    List<Review> findByRestaurantId(Long restaurantId);

    boolean existsByOrderAndUserAndParentReviewIsNull(Order order, User user);
}
