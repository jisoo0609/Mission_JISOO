package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShopId(Long id);
    Optional<Product> findByShopIdAndId(Long shopId, Long productId);

    // 이름 기준으로 상품 조회
    List<Product> findByNameContaining(String name);
    // 가격 범위에 해당하는 상품 조회
    List<Product> findByPriceBetween(Integer min, Integer max);
}
