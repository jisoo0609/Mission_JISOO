package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
