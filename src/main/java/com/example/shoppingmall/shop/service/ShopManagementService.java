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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

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

    // UPDATE IMAGE
    public ProductDto updateProductImage(Long shopId, Long productId, MultipartFile image) {
        // 업데이트할 대상 product 가져옴
        Product target = getProduct(shopId, productId);

        log.info("auth user: {}", authFacade.getAuthName());
        String owner = target.getShop().getUser().getUsername();
        log.info("shop Owner: {}", owner);
        // 접근한 user와 shop의 user가 일치하는지 확인
        if (!authFacade.getAuthName().equals(owner)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다.");
        }

        // 파일 업로드 위치 정하기
        String productDir = String.format("media/Product/%d/", productId);
        log.info(productDir);

        try {
            Files.createDirectories(Path.of(productDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 파일 이름 경로, 확장자 포함
        String originalFileName = image.getOriginalFilename();
        String[] fileNameSplit = originalFileName.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length -1];
        String productFileName = "Product." + extension;
        log.info(productFileName);

        String productPath = productDir + productFileName;
        log.info(productPath);

        // 저장
        try {
            image.transferTo(Path.of(productPath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String requestPath = String.format("/static/%d/%s", productId, productFileName);
        log.info(requestPath);
        target.setImage(requestPath);

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
