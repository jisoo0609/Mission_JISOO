package com.example.shoppingmall.shop.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PREPARING("구매 요청"),
    ACCEPT("수락"),
    REJECT("거절"),
    CANCEL("구매 취소"),
    COMPLETE("구매 완료");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
