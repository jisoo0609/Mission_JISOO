package com.example.shoppingmall.shop.dto;

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
    private Long productCategoryId;
    private Long shopId;
    private List<Long> orderId;
}
