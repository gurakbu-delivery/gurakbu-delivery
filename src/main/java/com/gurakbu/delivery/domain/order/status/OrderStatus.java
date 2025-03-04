package com.gurakbu.delivery.domain.order.status;

public enum OrderStatus {

    ORDER_RECEIVING,   // 주문 확인중
    ORDER_ACCEPTED,   //  주문 접수
    COOKING_IN_PROGRESS,    // 조리중
    COOKING_FINISHED,       // 조리 완료
    DELIVERY_IN_PROGRESS,   // 배달 중
    DELIVERY_FINISHED,     // 배달 완료
    CANCELLED,    // 주문 취소
}
