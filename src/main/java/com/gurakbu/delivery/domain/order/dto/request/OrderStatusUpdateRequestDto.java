package com.gurakbu.delivery.domain.order.dto.request;

import com.gurakbu.delivery.domain.order.status.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateRequestDto {

    @NotNull
    private OrderStatus status;
}
