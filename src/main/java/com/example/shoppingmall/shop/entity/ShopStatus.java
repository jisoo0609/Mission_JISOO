package com.example.shoppingmall.shop.entity;

import lombok.Getter;

@Getter
public enum ShopStatus {
    PREPARING("준비중"),
    SUBMITTED("개설 신청"),
    ACCEPT("수락"),
    REJECT("거절"),
    OPEN("오픈"),
    CLOSED("폐쇄");

    private final String status;

    ShopStatus(String status) {
        this.status = status;
    }
}
