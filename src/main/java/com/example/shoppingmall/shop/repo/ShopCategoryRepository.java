package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopCategoryRepository extends JpaRepository<ShopCategory, Long> {
    ShopCategory findByName(String name);
}
