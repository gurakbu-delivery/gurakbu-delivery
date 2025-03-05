package com.gurakbu.delivery.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReviewReplyRequestDto {

    @NotBlank(message = "답글 내용을 작성해주세요.")
    private String contents;
}
