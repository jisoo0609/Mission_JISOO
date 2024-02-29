package com.example.shoppingmall.shop.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.shop.dto.OrderDto;
import com.example.shoppingmall.shop.dto.OrderProductDto;
import com.example.shoppingmall.shop.entity.Order;
import com.example.shoppingmall.shop.entity.OrderStatus;
import com.example.shoppingmall.shop.entity.Product;
import com.example.shoppingmall.shop.repo.OrderRepository;
import com.example.shoppingmall.shop.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authFacade;

    // 쇼핑몰 상품 구매
    public OrderDto createOrder(Long shopId, Long productId, OrderProductDto dto) {
        // 구매할 상품 가져옴
        Product product = getProduct(shopId, productId);

        if (product.getStock() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "재고가 부족합니다.");
        }

        // 주문 생성하는 유저 정보
        UserEntity user = getUserEntity();

        // 주문하는 유저와 shop의 주인이 같으면 예외
        log.info("auth User: {}", user.getUsername());
        log.info("product owner: {}", product.getShop().getUser().getUsername());
        if (user.getUsername().equals(product.getShop().getUser().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자신이 등록한 물건을 구입할 수 없습니다.");
        }

        // 주문 생성
        Order newOrder = Order.builder()
                .totalPrice(dto.getCount() * product.getPrice())
                .status(OrderStatus.PREPARING)
                .orderDateTime(LocalDateTime.now())
                .user(user)
                .build();

        return OrderDto.fromEntity(orderRepository.save(newOrder));
    }

    private Product getProduct(Long shopId, Long productId) {
        Optional<Product> optionalProduct = productRepository.findByShopIdAndId(shopId, productId);
        if (optionalProduct.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return optionalProduct.get();
    }

    private UserEntity getUserEntity() {
        String authName = authFacade.getAuthName();
        return userRepository.findByUsername(authName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
