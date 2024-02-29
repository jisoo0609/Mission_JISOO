package com.example.shoppingmall.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;    // 상품 이름
    private String image;   // 상품 이미지
    private String description; // 상품 설명
    private Integer price;  // 상품 가격
    private Integer stock;  // 상품 재고

//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private ProductCategory productCategory;    // 상품 분류

    @ManyToOne
    private Shop shop;

    @ManyToMany(mappedBy = "products")
    private final List<Order> orders = new ArrayList<>();
}
