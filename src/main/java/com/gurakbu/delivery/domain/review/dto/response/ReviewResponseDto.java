package com.gurakbu.delivery.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private String contents;
    private Integer rating;
    private String restaurant;
    private String menu;
    private String user;

}
