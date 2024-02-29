package com.example.shoppingmall.shop.dto;

import com.example.shoppingmall.shop.entity.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String description;
    private ShopStatus status;
    private Long userId;
    private List<Long> shopCategoryId;

}