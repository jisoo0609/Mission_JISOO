package com.example.shoppingmall.shop.service;

import com.example.shoppingmall.shop.dto.ProductDto;
import com.example.shoppingmall.shop.dto.ShopDto;
import com.example.shoppingmall.shop.entity.Product;
import com.example.shoppingmall.shop.entity.Shop;
import com.example.shoppingmall.shop.repo.ProductRepository;
import com.example.shoppingmall.shop.repo.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    // 쇼핑몰 조회
    // 조건 X
    public List<ShopDto> readAllShop() {
        List<Shop> shopList = shopRepository.findByOrderByOrders_OrderDateTimeDesc();
        if (shopList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return shopList.stream()
                .map(ShopDto::fromEntity)
                .distinct()
                .collect(Collectors.toList());
    }

    // 쇼핑몰 조회
    // 이름
    public List<ShopDto> searchByShopName(String name) {
        List<Shop> shopList = shopRepository.findByNameContaining(name);
        if (shopList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return shopList.stream()
                .map(ShopDto::fromEntity)
                .collect(Collectors.toList());

    }

    // 쇼핑몰 조회
    // 카테고리
    public List<ShopDto> searchByShopCategory(List<String> categoryNames) {
        List<Shop> shopList = shopRepository.findByShopCategoriesName(categoryNames);
        if (shopList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return shopList.stream()
                .map(ShopDto::fromEntity)
                .collect(Collectors.toList());
    }

    // READ ALL
    // 쇼핑몰 상품 전체 보기
    public List<ProductDto> readAll(Long id) {
        Shop shop = getShop(id);
        List<Product> productList = productRepository.findByShopId(shop.getId());

        return productList.stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    // READ ONE
    // 상품 상세조회
    public ProductDto readOne(Long shopId, Long productId) {
        return ProductDto.fromEntity(getProduct(shopId, productId));
    }

    // 이름 기준으로 쇼핑몰의 상품 조회
    public List<ProductDto> searchByProductName(String name) {
        // 상품 조회
        List<Product> productList = productRepository.findByNameContaining(name);
        if (productList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return productList.stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 가격 볌위 기준으로 쇼핑몰의 상품 조회
    public List<ProductDto> searchByPriceRange(Integer min, Integer max) {
        // 상품 조회
        List<Product> productList = productRepository.findByPriceBetween(min, max);
        if (productList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return productList.stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    private Shop getShop(Long id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // shop 정보 확인용 log
        log.info("shop owner: {}", shop.getUser().getUsername());
        return shop;
    }

    private Product getProduct(Long shopId, Long productId) {
        Optional<Product> optionalProduct = productRepository.findByShopIdAndId(shopId, productId);
        if (optionalProduct.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return optionalProduct.get();
    }
}
