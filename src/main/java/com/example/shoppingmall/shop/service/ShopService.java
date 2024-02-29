package com.example.shoppingmall.shop.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.shop.dto.ShopDto;
import com.example.shoppingmall.shop.entity.Shop;
import com.example.shoppingmall.shop.entity.ShopCategory;
import com.example.shoppingmall.shop.entity.ShopStatus;
import com.example.shoppingmall.shop.repo.ShopCategoryRepository;
import com.example.shoppingmall.shop.repo.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final ShopCategoryRepository shopCategoryRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authFacade;

    // CREATE
    // 쇼핑몰 개설
    public ShopDto crateShop(ShopDto dto) {
        // 유저 불러 오기
        UserEntity user = getUserEntity();

        // 쇼핑몰 생성
        Shop newShop = Shop.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .user(user)
                .status(ShopStatus.PREPARING)
                .build();

        // 쇼핑몰 카테고리 추가
        List<ShopCategory> shopCategories = new ArrayList<>();
        for (String categoryName : dto.getShopCategories()) {
            ShopCategory shopCategory = shopCategoryRepository.findByName(categoryName);
            if (shopCategory == null) {
                // 쇼핑몰 카테고리가 존재하지 않는 경우 새로 생성
                shopCategory = new ShopCategory(categoryName);
                shopCategoryRepository.save(shopCategory);
            }
            shopCategories.add(shopCategory);
        }

        // 새로운 shop 생성
        newShop.setShopCategories(shopCategories);
        return ShopDto.fromEntity(shopRepository.save(newShop));
    }

    private UserEntity getUserEntity() {
        String authName = authFacade.getAuthName();
        UserEntity user = userRepository.findByUsername(authName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 유저 확인용 log
        log.info("name: {}", authName);
        log.info("Optional user Auth: {}", user.getAuthorities());

        return user;
    }


}
