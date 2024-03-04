package com.example.shoppingmall.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "order_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer count;  // 상품 수량

    // tossPayment 정보
    private String tossPaymentKey;
    private String tossOrderId;

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
