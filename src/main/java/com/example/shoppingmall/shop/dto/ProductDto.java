package com.example.shoppingmall.shop.dto;

import com.example.shoppingmall.shop.entity.Order;
import com.example.shoppingmall.shop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String image;
    private String description;
    private Integer price;
    private Integer stock;
    private Long shopId;
    private List<Long> orderId;
    private ShopDto shop;

    public static ProductDto fromEntity(Product entity) {
        List<Long> orderIdList = entity.getOrders().stream()
                .map(Order::getId)
                .toList();

        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .image(entity.getImage())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .shopId(entity.getShop().getId())
                .orderId(orderIdList)
                .shop(ShopDto.fromEntity(entity.getShop()))
                .build();
    }
}
