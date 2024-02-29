package com.example.shoppingmall.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShopCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "shopCategories")
    private List<Shop> shops;

    public ShopCategory(String name) {
        this.name = name;
    }
}
