package com.gurakbu.delivery.domain.order.controller;

import com.gurakbu.delivery.domain.order.dto.request.OrderRequestDto;
import com.gurakbu.delivery.domain.order.dto.request.OrderStatusUpdateRequestDto;
import com.gurakbu.delivery.domain.order.dto.response.OrderResponseDto;
import com.gurakbu.delivery.domain.order.service.OrderService;
import com.gurakbu.delivery.domain.order.status.OrderStatus;
import com.gurakbu.delivery.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody OrderRequestDto orderRequestDto){

        OrderResponseDto orderResponseDto = orderService.createOrder(user, orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }


    // 주문 상태 변경
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderStatus> updateOrderStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequestDto requestDto){

        OrderStatus updatedStatus = orderService.updateOrderStatus(user,orderId,requestDto.getStatus());

        return ResponseEntity.ok(updatedStatus);
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId){
        OrderResponseDto responseDto = orderService.getOrder(orderId);

        return ResponseEntity.ok(responseDto);
    }

}
