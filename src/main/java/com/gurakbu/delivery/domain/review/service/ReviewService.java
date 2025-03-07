package com.gurakbu.delivery.domain.review.service;

import com.gurakbu.delivery.domain.order.entity.Order;
import com.gurakbu.delivery.domain.order.repository.OrderRepository;
import com.gurakbu.delivery.domain.order.status.OrderStatus;
import com.gurakbu.delivery.domain.review.dto.request.ReviewReplyRequestDto;
import com.gurakbu.delivery.domain.review.dto.request.ReviewRequestDto;
import com.gurakbu.delivery.domain.review.dto.response.ReviewResponseDto;
import com.gurakbu.delivery.domain.review.entity.Review;
import com.gurakbu.delivery.domain.review.repository.ReviewRepository;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ReviewResponseDto createReview(User user, ReviewRequestDto requestDto) {
        // orderId로 주문 조회
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        if (!order.getRestaurant().getId().equals(requestDto.getRestaurantId())) {
            throw new IllegalArgumentException("주문과 가게 정보가 일치하지 않습니다.");
        }
        boolean menuMatch = order.getOrderItems().stream()
                .anyMatch(item -> item.getMenu().getId().equals(requestDto.getMenuId()));
        if (!menuMatch) {
            throw new IllegalArgumentException("해당 주문에 선택한 메뉴가 포함되어 있지 않습니다.");
        }

        // 해당 주문의 소유자(주문한 사용자)가 맞는지 검증
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("리뷰 작성 권한이 없습니다.");
        }

        // 배달 완료 상태에서만 리뷰 작성 가능
        if (order.getStatus() != OrderStatus.DELIVERY_FINISHED) {
            throw new IllegalArgumentException("배달이 완료된 주문에 대해서만 리뷰 작성이 가능합니다.");
        }

        // 한 주문 당 한 개의 리뷰만 허용
        boolean exists = reviewRepository.existsByOrderAndUserAndParentReviewIsNull(order, user);
        if (exists) {
            throw new IllegalArgumentException("한 주문 당 한 개의 리뷰만 작성할 수 있습니다.");
        }

        // 리뷰 생성 및 저장
        Review review = Review.builder()
                .rating(requestDto.getRating())
                .contents(requestDto.getContents())
                .user(user)
                .order(order)
                .restaurant(order.getRestaurant())
                .build();

        Review savedReview = reviewRepository.save(review);
        return convertToReviewResponseDto(savedReview);
    }

    // 리뷰에 대한 답글 남기기(사장님만 가능)
    @Transactional
    public ReviewResponseDto createReviewReply(User user , Long parentReviewId, ReviewReplyRequestDto requestDto){
        Review parentReview = reviewRepository.findById(parentReviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 없습니다."));

        if(!user.isOwner(parentReview.getRestaurant().getId())){
            throw new IllegalArgumentException("답글 작성 권한이 없습니다.");
        }

        if(parentReview.getChildReviews() != null & !parentReview.getChildReviews().isEmpty()){
            throw new IllegalArgumentException("이미 답글이 작성되었습니다.");
        }

        Review reply = new Review();

        reply.setContents(requestDto.getContents());
        reply.setRating(0);
        reply.setUser(user);
        reply.setOrder(parentReview.getOrder());
        reply.setRestaurant(parentReview.getRestaurant());
        reply.setParentReview(parentReview);

        Review savedReview = reviewRepository.save(reply);
        return convertChildReviewToDto(savedReview);
    }


    @Transactional
    public List<ReviewResponseDto> getAllReviews(Long restaurantId,Integer minRating, Integer maxRating,String sort){

        List<Review> reviews;
        if(restaurantId != null){
            reviews = reviewRepository.findByRestaurantId(restaurantId);
        }else{
            throw new IllegalArgumentException("가게를 찾을 수 없습니다.");
        }


        if(minRating != null && maxRating != null){
            reviews = reviews.stream()
                    .filter(r -> r.getRating() >= minRating && r.getRating() <= maxRating)
                    .toList();
        }else if(minRating != null){
            reviews = reviews.stream()
                    .filter(r->r.getRating() >= minRating)
                    .toList();
        }else if(maxRating != null){
            reviews = reviews.stream()
                    .filter(r -> r.getRating() <= maxRating)
                    .toList();
        }

        if("latest".equalsIgnoreCase(sort)){
            reviews.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }else if("oldest".equalsIgnoreCase(sort)){
            reviews.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }

        return reviews.stream()
                .map(this::convertToReviewResponseDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto,User user){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if(!review.getUser().getId().equals(user.getId()) && !user.isAdmin()){
            throw new SecurityException("권한이 없습니다.");
        }

        review.setContents(requestDto.getContents());
        review.setRating(requestDto.getRating());

        Review updatedReview = reviewRepository.save(review);

        return convertToReviewResponseDto(updatedReview);
    }

    @Transactional
    public ReviewResponseDto updateReviewReply(User user, Long parentReviewId, ReviewReplyRequestDto requestDto){

        Review parentReview = reviewRepository.findById(parentReviewId)
                .orElseThrow(()-> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if(!user.isOwner(parentReview.getRestaurant().getId())){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        if(parentReview.getChildReviews() != null || !parentReview.getChildReviews().isEmpty()){
            throw new IllegalArgumentException("답글이 없습니다.");
        }

        Review reply = parentReview.getChildReviews().get(0);
        reply.setContents(requestDto.getContents());

        Review updatedReview = reviewRepository.save(reply);
        return convertChildReviewToDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId,User user){

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if(!review.getUser().getId().equals(user.getId()) && !user.isAdmin()){
            throw new SecurityException("권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }


    private ReviewResponseDto convertToReviewResponseDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setReviewId(review.getId());
        dto.setContents(review.getContents());
        dto.setRating(review.getRating());
        dto.setUserName(review.getUser().getName());


        // 주문 정보
        dto.setOrderId(review.getOrder().getId());
        dto.setRestaurantName(review.getRestaurant().getName());

        // 주문 항목 목록
        List<String> menuNames = review.getOrder().getOrderItems().stream()
                .map(orderItem -> orderItem.getMenu().getName())
                .toList(); // or collect(Collectors.toList())
        dto.setMenuNames(menuNames);

        // 사용자 리뷰 조회 시 사장님 답글이 있으면 같이 포함
        if(review.getChildReviews() != null && !review.getChildReviews().isEmpty()){

            Review childReview = review.getChildReviews().get(0);
            dto.setReply(convertChildReviewToDto(childReview));
        }

        return dto;
    }

    private ReviewResponseDto convertChildReviewToDto(Review child) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setReviewId(child.getId());
        dto.setContents(child.getContents());
        dto.setRating(child.getRating());
        dto.setUserName(child.getUser().getName());
        dto.setOrderId(child.getOrder().getId());
        dto.setRestaurantName(child.getRestaurant().getName());
        dto.setMenuNames(Collections.emptyList());

        return dto;
    }
}
