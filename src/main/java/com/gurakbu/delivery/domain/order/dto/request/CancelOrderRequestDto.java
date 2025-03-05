package com.gurakbu.delivery.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelOrderRequestDto {

    @NotBlank(message = "취소 사유를 입력해주세요")
    private String reason;

}
