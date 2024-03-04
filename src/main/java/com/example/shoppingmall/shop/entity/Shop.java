package com.example.shoppingmall.shop.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;   // 쇼핑몰 이름
    private String description;   // 소개
    @Enumerated(EnumType.STRING)
    private ShopStatus status;  // 상태
    private String closeReason;   // 폐쇄 사유
    private String rejectionReason; // 불허 사유

    @ManyToOne
    private UserEntity user;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "shop_shopcategory",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "shopcategory_id")
    )
    private Set<ShopCategory> shopCategories;
}
