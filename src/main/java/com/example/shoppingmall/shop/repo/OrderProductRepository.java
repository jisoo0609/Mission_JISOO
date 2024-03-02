package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Optional<OrderProduct> findByOrder_IdAndProduct_Id(Long orderId, Long productId);
}
