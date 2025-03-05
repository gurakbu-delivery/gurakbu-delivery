package com.gurakbu.delivery.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @NotNull
    private Long restaurantId;

    @NotNull
    private List<OrderItemRequestDto> orderItems;

    @Getter
    @Setter
    public static class OrderItemRequestDto {
        private Long menuId;

        private Integer quantity;
    }

}
