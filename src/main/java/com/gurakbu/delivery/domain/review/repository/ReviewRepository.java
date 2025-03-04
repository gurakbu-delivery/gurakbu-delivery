package com.gurakbu.delivery.domain.review.repository;

import com.gurakbu.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAll();

    List<Review> findByUser(Long userId);

}
