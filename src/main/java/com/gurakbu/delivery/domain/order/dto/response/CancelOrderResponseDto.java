package com.gurakbu.delivery.domain.order.dto.response;

import com.gurakbu.delivery.domain.order.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CancelOrderResponseDto extends OrderResponseDto {

    private String cancelReason;
}
