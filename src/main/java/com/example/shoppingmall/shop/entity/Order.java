package com.example.shoppingmall.shop.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "custom_order")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer totalPrice; // 금액
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태
    private LocalDateTime orderDateTime;    // 주문 시각

    @ManyToOne
    private UserEntity user;    // 주문한 유저

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;    // 주문이 발생한 Shop

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private final List<Product> products = new ArrayList<>();
}
