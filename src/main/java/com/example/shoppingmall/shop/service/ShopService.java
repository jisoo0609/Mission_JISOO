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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
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
        Set<ShopCategory> shopCategories = getShopCategories(new HashSet<>(dto.getShopCategories()));

        // 새로운 shop 생성
        newShop.setShopCategories(shopCategories);
        return ShopDto.fromEntity(shopRepository.save(newShop));
    }

    // 쇼핑몰 정보 불러오기
    public ShopDto readOne(Long id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Shop shop = optionalShop.get();

        log.info("shop Owner: {}", shop.getUser().getUsername());
        log.info("auth user: {}", authFacade.getAuthName());
        if (!shop.getUser().getUsername().equals(authFacade.getAuthName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return ShopDto.fromEntity(shop);
    }

    // 쇼핑몰 정보 수정
    public ShopDto updateShop(Long id, ShopDto dto) {
        // 수정할 Shop 가져옴
        Shop target = getShop(id);

        // shop owner
        String user = target.getUser().getUsername();

        log.info("shop Owner: {}", user);
        log.info("auth user: {}", authFacade.getAuthName());
        // 접근한 user와 shop owner가 같을때 정보 수정 가능
        if (!user.equals(authFacade.getAuthName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 카테고리 정보 수정
        Set<ShopCategory> shopCategories
                = getShopCategories(new HashSet<>(dto.getShopCategories()));
        target.setName(dto.getName());
        target.setDescription(dto.getDescription());
        target.setShopCategories(shopCategories);

        return ShopDto.fromEntity(shopRepository.save(target));
    }

    // 쇼핑몰 개설 요청
    public ShopStatus openRequest(Long id) {
        // 개설 요청할 쇼핑몰 정보 불러오기
        Shop shop = getShop(id);

        // 쇼핑몰 owner와, 접근 user가 같은 경우 요청 가능
        log.info("shop Owner: {}", shop.getUser().getUsername());
        log.info("auht user: {}", authFacade.getAuthName());
        if (!shop.getUser().getUsername().equals(authFacade.getAuthName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 이미 오픈된 쇼핑몰이 요청을 보냈을 경우 예외
        if (shop.getStatus().equals(ShopStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        shop.setStatus(ShopStatus.SUBMITTED);
        shopRepository.save(shop);
        return shop.getStatus();
    }

    // 쇼핑몰 폐쇄 요청
    public String closeRequest(Long id, ShopDto dto) {
        // 폐쇄 요청할 쇼핑몰 정보 불러오기
        Shop shop = getShop(id);

        // 쇼핑몰 owner와, 접근 user가 같은 경우 요청 가능
        log.info("shop Owner: {}", shop.getUser().getUsername());
        log.info("auth user: {}", authFacade.getAuthName());
        if (!shop.getUser().getUsername().equals(authFacade.getAuthName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 오픈하지 않은 쇼핑몰이 요청을 보냈을 경우 예외
        if (!shop.getStatus().equals(ShopStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        shop.setCloseReason(dto.getCloseReason());
        log.info("reason: {}", shop.getCloseReason());
        shopRepository.save(shop);
        return shop.getCloseReason();
    }

    // 개설 신청된 쇼핑몰 목록 확인
    public List<ShopDto> openRequestShopList() {
        List<Shop> requestList = shopRepository.findByStatusEquals(ShopStatus.SUBMITTED);

        return requestList.stream()
                .map(ShopDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 관리자가 개설 허가 또는 거절
    @Transactional
    public ShopStatus accept(Long id, boolean flag, ShopDto dto) {
        // 개설 허가 또는 거절할 shop 불러옴
        Shop shop = getShop(id);

        // shop의 상태가 SUBMIT인 경우만 승인 가능
        if (shop.getStatus().equals(ShopStatus.SUBMITTED)) {
            if (flag) {
                shop.setStatus(ShopStatus.ACCEPT);
                log.info("request accept: {}", shop.getStatus());
                // 개설이 승인된 쇼핑몰의 상태는 오픈으로 변경
                shop.setStatus(ShopStatus.OPEN);
                log.info("open shop: {}", shop.getStatus());
                shopRepository.save(shop);
            } else {
                shop.setStatus(ShopStatus.REJECT);
                shop.setRejectionReason(dto.getRejectionReason());
                shopRepository.save(shop);
            }
            return shop.getStatus();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // 관리자가 폐쇄 수락
    @Transactional
    public ShopStatus closeAccept(Long id, boolean flag) {
        // 폐쇄 요청이 온 쇼핑몰 가져오기
        Shop shop = getShop(id);

        if (shop.getCloseReason() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (flag) {
            shop.setStatus(ShopStatus.CLOSED);
            shopRepository.save(shop);
        } else {
            // 폐쇄 거절
            shop.setCloseReason(null);
            shopRepository.save(shop);
        }
        return shop.getStatus();
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

    private Shop getShop(Long id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // shop 정보 확인용 log
        log.info("shop owner: {}", shop.getUser().getUsername());
        return shop;
    }

    // 쇼핑몰 카테고리 추출 메서드
    private Set<ShopCategory> getShopCategories(Set<String> categoryNames) {
        Set<ShopCategory> shopCategories = new HashSet<>();
        for (String categoryName : categoryNames) {
            ShopCategory shopCategory = shopCategoryRepository.findByName(categoryName);
            if (shopCategory == null) {
                // 쇼핑몰 카테고리가 존재하지 않는 경우 새로 생성
                shopCategory = new ShopCategory(categoryName);
                shopCategoryRepository.save(shopCategory);
            }
            shopCategories.add(shopCategory);
        }
        return shopCategories;
    }
}
