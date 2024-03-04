package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByOrderByOrders_OrderDateTimeDesc();
}
