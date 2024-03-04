package com.example.shoppingmall.shop.dto;

import lombok.Data;

@Data
public class PaymentConfirmDto {
    private String paymentKey;
    private String orderId;
    private Integer amount;
}
