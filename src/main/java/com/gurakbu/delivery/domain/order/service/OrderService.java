package com.gurakbu.delivery.domain.order.service;

import com.gurakbu.delivery.domain.menu.entity.Menu;
import com.gurakbu.delivery.domain.menu.repository.MenuRepository;
import com.gurakbu.delivery.domain.order.dto.request.OrderRequestDto;
import com.gurakbu.delivery.domain.order.dto.response.OrderResponseDto;
import com.gurakbu.delivery.domain.order.entity.Order;
import com.gurakbu.delivery.domain.order.entity.OrderItem;
import com.gurakbu.delivery.domain.order.repository.OrderRepository;
import com.gurakbu.delivery.domain.order.status.OrderStatus;
import com.gurakbu.delivery.domain.restaurant.entity.Restaurant;
import com.gurakbu.delivery.domain.restaurant.repository.RestaurantRepository;
import com.gurakbu.delivery.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;

    private final MenuRepository menuRepository;

    public OrderResponseDto createOrder(User user, OrderRequestDto orderRequestDto) {

        // 가게 조회
        Restaurant restaurant = restaurantRepository.findById(orderRequestDto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 주문 생성
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.ORDER_RECEIVING);

        int totalPrice = 0 ;

        // 주문 요청 시 주문 항목 처리
        for(OrderRequestDto.OrderItemRequestDto orderItemRequestDto : orderRequestDto.getOrderItems()) {

            Menu menu = menuRepository.findById(orderItemRequestDto.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

            // 주문 항목 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenu(menu);
            orderItem.setQuantity(orderItemRequestDto.getQuantity());
            orderItem.setPrice(menu.getPrice());

            order.getOrderItems().add(orderItem);

            totalPrice += menu.getPrice() * orderItemRequestDto.getQuantity();
        }

        order.setTotal_price(totalPrice);

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(savedOrder.getId());
        orderResponseDto.setRestaurantName(restaurant.getName());
        orderResponseDto.setRestaurantAddress(restaurant.getAddress());
        orderResponseDto.setTotalPrice(savedOrder.getTotal_price());
        orderResponseDto.setOrderStatus(savedOrder.getStatus());

        List<OrderResponseDto.OrderItemResponseDto> orderItemResponseList
                = new ArrayList<>();

        savedOrder.getOrderItems().forEach(orderItem -> {
            OrderResponseDto.OrderItemResponseDto itemDto = new OrderResponseDto.OrderItemResponseDto();

            itemDto.setMenuName(orderItem.getMenu().getName());
            itemDto.setQuantity(orderItem.getQuantity());
            itemDto.setPrice(orderItem.getPrice());
            orderItemResponseList.add(itemDto);
        });
        orderResponseDto.setOrderItems(orderItemResponseList);

        return orderResponseDto;
    }


    // 주문 상태변경
    public OrderStatus updateOrderStatus(User user,Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new IllegalArgumentException("주문을 확인할 수 없습니다."));

        order.setStatus(orderStatus);
        orderRepository.save(order);

        return order.getStatus();
    }

    // 주문 상세 조회
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 확인할 수 없습니다."));

        OrderResponseDto orderResponseDto = new OrderResponseDto();

        orderResponseDto.setOrderId(order.getId());
        orderResponseDto.setRestaurantName(order.getRestaurant().getName());
        orderResponseDto.setRestaurantAddress(order.getRestaurant().getAddress());
        orderResponseDto.setTotalPrice(order.getTotal_price());
        orderResponseDto.setOrderStatus(order.getStatus());

        List<OrderResponseDto.OrderItemResponseDto> orderItemList = order.getOrderItems()
                .stream()
                .map(item ->{
                    OrderResponseDto.OrderItemResponseDto itemDto = new OrderResponseDto.OrderItemResponseDto();
                    itemDto.setMenuName(item.getMenu().getName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    return itemDto;
                });

        orderResponseDto.setOrderItems(orderItemList);

        return orderResponseDto;
    }


    public void cancelOrderByRestaurant(User user, Long restaurantId,Long orderId,String reason) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

        // 주문이 해당 restaurantId 소유인지 확인
        if (!order.getRestaurant().getId().equals(restaurantId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"가게의 주문이 아닙니다.");
        }

        if(!user.isAdmin() && !user.isOwner(restaurantId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"권한이 없습니다.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(reason);
        orderRepository.save(order);
    }
}
