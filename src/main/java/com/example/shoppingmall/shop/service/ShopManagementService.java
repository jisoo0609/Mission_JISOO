package com.example.shoppingmall.shop.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.shop.dto.ProductDto;
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
public class ShopManagementService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final AuthenticationFacade authFacade;

    // CREATE
    // 상품 등록
    public ProductDto crateProduct(Long id, ProductDto dto) {
        // 쇼핑몰 불러오기
        Shop shop = getShop(id);

        log.info("auth user: {}", authFacade.getAuthName());
        log.info("shop Owner: {}", shop.getUser().getUsername());
        // 접근한 user와 shop의 user가 일치하는지 확인
        if (!authFacade.getAuthName().equals(shop.getUser().getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다.");
        }

        // 상품 등록
        Product newProduct = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .image(dto.getImage())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .shop(shop)
                .build();

        return ProductDto.fromEntity(productRepository.save(newProduct));
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
        Optional<Product> optionalProduct = productRepository.findByShopIdAndId(shopId, productId);
        if (optionalProduct.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Product product = optionalProduct.get();

        return ProductDto.fromEntity(product);
    }

    // UPDATE
    // 상품 수정
    public ProductDto updateProduct(Long shopId, Long productId, ProductDto dto) {
        // 수정할 대상 product 가져옴
        Product target = getProduct(shopId, productId);

        log.info("auth user: {}", authFacade.getAuthName());
        String owner = target.getShop().getUser().getUsername();
        log.info("shop Owner: {}", owner);
        // 접근한 user와 shop의 user가 일치하는지 확인
        if (!authFacade.getAuthName().equals(owner)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다.");
        }

        target.setName(dto.getName());
        target.setDescription(dto.getDescription());
        target.setImage(dto.getImage());
        target.setPrice(dto.getPrice());
        target.setStock(dto.getStock());

        return ProductDto.fromEntity(productRepository.save(target));
    }

    // DELETE
    // 상품 삭제
    public void deleteProduct(Long shopId, Long productId) {
        // 삭제할 대상 product 가져옴
        Product target = getProduct(shopId, productId);

        log.info("auth user: {}", authFacade.getAuthName());
        String owner = target.getShop().getUser().getUsername();
        log.info("shop Owner: {}", owner);
        // 접근한 user와 shop의 user가 일치하는지 확인
        if (!authFacade.getAuthName().equals(owner)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다.");
        }

        productRepository.delete(target);
    }

    // SEARCH
    // 쇼핑몰 상품 조회


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
