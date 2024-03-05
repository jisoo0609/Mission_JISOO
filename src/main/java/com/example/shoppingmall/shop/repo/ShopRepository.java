package com.example.shoppingmall.shop.repo;

import com.example.shoppingmall.shop.entity.Shop;
import com.example.shoppingmall.shop.entity.ShopStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    // open요청 보낸 쇼핑몰 조회
    List<Shop> findByStatusEquals(ShopStatus status);

    // 최신 주문 순으로 쇼핑몰 조회
    List<Shop> findByOrderByOrders_OrderDateTimeDesc();

    // 이름 기준으로 쇼핑몰 조회
    List<Shop> findByNameContaining(String name);

    // 분류 기준으로 쇼핑몰 조회
    @Query("SELECT s FROM Shop s JOIN s.shopCategories c WHERE c.name IN :categoryNames")
    List<Shop> findByShopCategoriesName(List<String> categoryNames);
}
