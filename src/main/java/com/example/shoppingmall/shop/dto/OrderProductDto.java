package com.example.shoppingmall.shop.dto;

import com.example.shoppingmall.shop.entity.OrderProduct;
import com.example.shoppingmall.shop.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderProductDto {
    private Long id;
    private Long OrderId;
    private Long productId;
    private Integer count;  // 상품 수량
    private String tossPaymentKey;
    private String tossOrderId;
    private OrderStatus status;

    public static OrderProductDto fromEntity(OrderProduct entity) {
        return OrderProductDto.builder()
                .id(entity.getId())
                .OrderId(entity.getOrder().getId())
                .productId(entity.getProduct().getId())
                .count(entity.getCount())
                .tossPaymentKey(entity.getTossPaymentKey())
                .tossOrderId(entity.getTossOrderId())
                .build();
    }
}
