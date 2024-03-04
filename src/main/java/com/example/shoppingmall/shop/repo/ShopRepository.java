package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    // 최신 주문 순으로 쇼핑몰 조회
    List<Shop> findByOrderByOrders_OrderDateTimeDesc();

    // 이름 기준으로 쇼핑몰 조회
    List<Shop> findByNameContaining(String name);
}
