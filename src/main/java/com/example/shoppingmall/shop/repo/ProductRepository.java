package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShopId(Long id);
    Optional<Product> findByShopIdAndId(Long shopId, Long productId);
}
