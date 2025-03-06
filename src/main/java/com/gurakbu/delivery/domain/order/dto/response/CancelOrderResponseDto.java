package com.gurakbu.delivery.domain.order.dto.response;

import com.gurakbu.delivery.domain.order.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CancelOrderResponseDto extends OrderResponseDto {

    private String cancelReason;


    public CancelOrderResponseDto(Long id, String name, String address, List<OrderItemResponseDto> collect, Integer totalPrice, OrderStatus status, String cancelReason) {
        super();
        this.cancelReason = cancelReason;
    }
}
