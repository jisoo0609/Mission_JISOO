package com.example.shoppingmall.shop.controller;

import com.example.shoppingmall.shop.dto.ProductDto;
import com.example.shoppingmall.shop.service.ShopManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shop/{shopId}/management")
@RequiredArgsConstructor
public class ShopManagementController {
    private final ShopManagementService service;

    // 쇼핑몰 상품 등록
    @PutMapping("/create")
    public ProductDto createProduct(
            @PathVariable("shopId") Long id,
            @RequestBody ProductDto dto)
    {
        return service.crateProduct(id, dto);
    }

    @PostMapping("/{productId}/update")
    public ProductDto updateProduct(
            @PathVariable("shopId") Long shopId,
            @PathVariable("productId") Long productId,
            @RequestBody ProductDto dto
    ) {
        return service.updateProduct(shopId, productId, dto);
    }

    @DeleteMapping("/{productId}/delete")
    public String deleteProduct(
            @PathVariable("shopId") Long shopId,
            @PathVariable("productId") Long productId
    ) {
        service.deleteProduct(shopId, productId);
        return "DELETE PRODUCT";
    }
}
