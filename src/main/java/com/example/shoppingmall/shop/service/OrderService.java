package com.example.shoppingmall.shop.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.shop.dto.OrderDto;
import com.example.shoppingmall.shop.dto.OrderProductDto;
import com.example.shoppingmall.shop.entity.*;
import com.example.shoppingmall.shop.repo.OrderProductRepository;
import com.example.shoppingmall.shop.repo.OrderRepository;
import com.example.shoppingmall.shop.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authFacade;

    // 쇼핑몰 상품 구매
    public OrderDto createOrder(Long shopId, Long productId, OrderProductDto dto) {
        // 구매할 상품 가져옴
        Product product = getProduct(shopId, productId);

        // 주문할 상품의 재고가 0개인 경우 예외
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

        // 주문 생성 및 저장
        Order newOrder = Order.builder()
                .totalPrice(dto.getCount() * product.getPrice())
                .status(OrderStatus.PREPARING)
                .orderDateTime(LocalDateTime.now())
                .shop(product.getShop())
                .user(user)
                .build();
        Order saveOrder = orderRepository.save(newOrder);

        // OrderProduct 생성 및 저장
        OrderProduct orderProduct = OrderProduct.builder()
                .order(saveOrder)
                .product(product)
                .count(dto.getCount())
                .build();
        orderProductRepository.save(orderProduct);

        return OrderDto.fromEntity(saveOrder);
    }

    // 구매 요청 수락
    @Transactional
    public OrderStatus accept(Long shopId, Long productId, Long orderId, boolean flag) {
        // 구매 요청 수락 / 거절될 주문 불러옴
        Order order = getOrder(orderId);
        Product product = getProduct(shopId, productId);

        // 주문 수락하는 유저 정보
        UserEntity user = getUserEntity();

        // 주문 수락하는 유저와 shop의 주인이 같지 않으면 예외
        log.info("auth User: {}", user.getUsername());
        log.info("product owner: {}", product.getShop().getUser().getUsername());
        if (!user.getUsername().equals(product.getShop().getUser().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자신이 등록한 물건을 구입할 수 없습니다.");
        }

        // 가격 확인
        log.info("total Price: {}", order.getTotalPrice());

        OrderProduct orderProduct = getOrderProduct(orderId, productId);

        // 구매 요청 수락
        if (flag) {
            // 재고 수량 갱신
            int stock = product.getStock() - orderProduct.getCount();
            product.setStock(stock);
            productRepository.save(product);

            // 구매 상태 변경
            order.setStatus(OrderStatus.ACCEPT);
            orderRepository.save(order);
        } else {
            order.setStatus(OrderStatus.REJECT);
            orderRepository.save(order);
        }
        return order.getStatus();
    }

    // 구매 취소 요청
    @Transactional
    public OrderStatus cancelRequest(Long id) {
        // 주문 취소 요청할 주문 불러옴
        Order order = getOrder(id);

        // 주문 취소하는 유저 정보
        UserEntity user = getUserEntity();

        // 주문 취소하는 user와 order 요청한 user가 다르면 예외
        log.info("auth User: {}", user.getUsername());
        log.info("order User: {}", order.getUser().getUsername());
        if (!user.getUsername().equals(order.getUser().getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "주문을 요청한 유저만 취소 요청 가능합니다.");
        }

        // 주문 상태가 수락이 아닐 경우에만 취소 가능
        if (order.getStatus().equals(OrderStatus.ACCEPT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 구매 요청이 수락된 주문입니다.");
        }

        // 구매 요청 취소
        order.setStatus(OrderStatus.CANCEL);
        orderRepository.save(order);

        return order.getStatus();
    }


    private Product getProduct(Long shopId, Long productId) {
        Optional<Product> optionalProduct = productRepository.findByShopIdAndId(shopId, productId);
        if (optionalProduct.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return optionalProduct.get();
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private OrderProduct getOrderProduct(Long orderId, Long productId) {
        return orderProductRepository.findByOrder_IdAndProduct_Id(orderId, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private UserEntity getUserEntity() {
        String authName = authFacade.getAuthName();
        return userRepository.findByUsername(authName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
