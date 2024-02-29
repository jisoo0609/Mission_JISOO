package com.example.shoppingmall.shop.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;   // 쇼핑몰 이름
    private String description;   // 소개
    @Enumerated(EnumType.STRING)
    private ShopStatus status;

    @ManyToOne
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "shop_shopcategory",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "shopcategory_id")
    )
    private List<ShopCategory> shopCategories;
}
