package com.example.shoppingmall.shop.controller;

import com.example.shoppingmall.shop.dto.ProductDto;
import com.example.shoppingmall.shop.dto.ShopDto;
import com.example.shoppingmall.shop.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class ProductController {
    private final SearchService service;

    // 조건 없이 쇼핑몰 조회
    @GetMapping
    public List<ShopDto> readAll() {
        return service.readAllShop();
    }

    // 쇼핑몰 이름 기준으로 쇼핑몰 조회
    @GetMapping("/shop")
    public List<ShopDto> searchByShopName(@RequestParam String name) {
        return service.searchByShopName(name);
    }

    // 쇼핑몰 카테고리 기준으로 쇼핑몰 조회
    @GetMapping("/category")
    public List<ShopDto> searchByShopCategory(@RequestParam List<String> name) {
        return service.searchByShopCategory(name);
    }

    // 쇼핑몰 상품 전체 조회
    @GetMapping("/shop/{shopId}/product")
    public List<ProductDto> readAll(@PathVariable("shopId") Long id) {
        return service.readAll(id);
    }

    // 쇼핑몰 상품 상세 조회
    @GetMapping("/shop/{shopId}/product/{productId}")
    public ProductDto readOne(
            @PathVariable("shopId") Long shopId,
            @PathVariable("productId") Long productId)
    {
        return service.readOne(shopId, productId);
    }

    // 이름 기준으로 상품 조회
    @GetMapping("/product")
    public List<ProductDto> searchByProductName(@RequestParam String name) {
        return service.searchByProductName(name);
    }

    // 가격 범위를 기준으로 상품 조회
    @GetMapping("/price")
    public List<ProductDto> searchByPriceRange(
            @RequestParam Integer min,
            @RequestParam Integer max
    ) {
        return service.searchByPriceRange(min, max);
    }
}
