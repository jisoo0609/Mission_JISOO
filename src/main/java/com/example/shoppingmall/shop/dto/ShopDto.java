package com.example.shoppingmall.shop.dto;

import com.example.shoppingmall.shop.entity.Shop;
import com.example.shoppingmall.shop.entity.ShopCategory;
import com.example.shoppingmall.shop.entity.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String description;
    private ShopStatus status;
    private Long userId;
    private Set<String> shopCategories;

    public static ShopDto fromEntity(Shop shop) {
        Set<String> categoryName = shop.getShopCategories().stream()
                .map(ShopCategory::getName)
                .collect(Collectors.toSet());

        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .status(shop.getStatus())
                .userId(shop.getUser().getId())
                .shopCategories(categoryName)
                .build();
    }

}