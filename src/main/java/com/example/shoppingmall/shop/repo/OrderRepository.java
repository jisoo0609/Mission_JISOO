package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
