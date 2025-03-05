package com.gurakbu.delivery.domain.review.controller;

import com.gurakbu.delivery.domain.review.dto.request.ReviewReplyRequestDto;
import com.gurakbu.delivery.domain.review.dto.request.ReviewRequestDto;
import com.gurakbu.delivery.domain.review.dto.response.ReviewResponseDto;
import com.gurakbu.delivery.domain.review.repository.ReviewRepository;
import com.gurakbu.delivery.domain.review.service.ReviewService;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;


    // 사용자 리뷰 작성
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid ReviewRequestDto requestDto
    ){

        ReviewResponseDto responseDto = reviewService.createReview(user, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 리뷰에 대한 사장님 답글 작성
    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<ReviewResponseDto> createReviewReply(
            @AuthenticationPrincipal User user,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewReplyRequestDto requestDto
            ){
        ReviewResponseDto responseDto = reviewService.createReviewReply(user, reviewId, requestDto);
        return ResponseEntity.ok(responseDto);
    }


    // 리뷰 다건 조회 (별점 범위,최신순 정렬)
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews(
            @RequestParam Long restaurantId,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating,
            @RequestParam(required = false,defaultValue = "latest") String sort
    ) {

        List<ReviewResponseDto> responseDto = reviewService.getAllReviews(restaurantId, minRating, maxRating, sort);
        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @AuthenticationPrincipal User user,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewRequestDto requestDto
    ){

        ReviewResponseDto responseDto = reviewService.updateReview(reviewId,requestDto,user);
        return ResponseEntity.ok(responseDto);
    }


    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal User user,
            @PathVariable Long reviewId
            ) {

        reviewService.deleteReview(reviewId,user);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }
}
