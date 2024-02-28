package com.example.shoppingmall.retrade.dto;

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
    private String status;
}
