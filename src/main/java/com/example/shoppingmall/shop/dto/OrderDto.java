package com.example.shoppingmall.shop.dto;

import com.example.shoppingmall.shop.entity.Order;
import com.example.shoppingmall.shop.entity.OrderStatus;
import com.example.shoppingmall.shop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Integer totalPrice;
    private OrderStatus status;
    private LocalDateTime orderDateTime;
    private Long shopId;
    private Long userId;
    private List<Long> productId;

    public static OrderDto fromEntity(Order entity) {
        List<Long> productIdList = entity.getProducts().stream()
                .map(Product::getId)
                .toList();
        return OrderDto.builder()
                .id(entity.getId())
                .totalPrice(entity.getTotalPrice())
                .status(entity.getStatus())
                .orderDateTime(entity.getOrderDateTime())
                .shopId(entity.getShop().getId())
                .userId(entity.getUser().getId())
                .productId(productIdList)
                .build();
    }
}

