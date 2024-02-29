package com.example.shoppingmall.shop.dto;

import com.example.shoppingmall.shop.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Integer totalPrice;
    private OrderStatus status;
    private Long userId;
    private List<Long> productId;
}
