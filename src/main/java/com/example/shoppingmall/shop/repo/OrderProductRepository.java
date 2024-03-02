package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
