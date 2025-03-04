package com.gurakbu.delivery.domain.review.service;

import com.gurakbu.delivery.domain.order.entity.Order;
import com.gurakbu.delivery.domain.order.repository.OrderRepository;
import com.gurakbu.delivery.domain.review.dto.request.ReviewRequestDto;
import com.gurakbu.delivery.domain.review.dto.response.ReviewResponseDto;
import com.gurakbu.delivery.domain.review.repository.ReviewRepository;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ReviewResponseDto createReview(User user, ReviewRequestDto requestDto){

        Order order = orderRepository.findByUserAndMenuId(user.getId(), requestDto.getMenuId()).
                orElseThrow(()->new IllegalArgumentException("주문을 확인할 수 없습니다."));

        if(!order.getStatus().equals("DELIVERY_FINI")){
            throw new IllegalArgumentException("리뷰를 작성할 수 없습니다.");
        }


    }
}
