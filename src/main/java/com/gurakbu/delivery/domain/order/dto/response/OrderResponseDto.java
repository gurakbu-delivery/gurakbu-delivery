package com.gurakbu.delivery.domain.order.dto.response;

import com.gurakbu.delivery.domain.order.status.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponseDto {

    private Long orderId;
    private String restaurantName;
    private String restaurantAddress;
    private List<OrderItemResponseDto> orderItems;
    private int totalPrice;
    private OrderStatus orderStatus;

    @Getter
    @Setter
    public static class OrderItemResponseDto {
        private String menuName;
        private int quantity;
        private int price;
    }
}
