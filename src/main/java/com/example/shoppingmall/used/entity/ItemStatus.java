package com.example.shoppingmall.used.entity;

import lombok.Getter;

@Getter
public enum ItemStatus {
    SALE("판매중"),
    ACCEPT("수락"),
    REJECT("거절"),
    CONFIRM("구매 확정"),
    SOLD("판매 완료");

    private final String status;

    ItemStatus(String status) {
        this.status = getStatus();
    }
}
