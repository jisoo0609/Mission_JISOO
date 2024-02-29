package com.example.shoppingmall.shop.entity;

public enum OrderStatus {
    PREPARING("구매 요청"),
    ACCEPT("수락"),
    REJECT("거절"),
    CANCEL("구매 취소");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
