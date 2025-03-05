package com.gurakbu.delivery.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private String contents;
    private Integer rating;
    private String userName;
    private Long orderId;
    private String restaurantName;
    private List<String> menuNames;

    private ReviewResponseDto reply;

}
