package com.example.shoppingmall.used.dto;

import com.example.shoppingmall.used.entity.Item;
import com.example.shoppingmall.used.entity.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Integer price;
    private ItemStatus status;
    private Long userId;

    public static ItemDto fromEntity(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .image(item.getImage())
                .price(item.getPrice())
                .status(item.getStatus())
                .userId(item.getUser().getId())
                .build();
    }
}
