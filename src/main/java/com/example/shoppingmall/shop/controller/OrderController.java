package com.example.shoppingmall.shop.controller;

import com.example.shoppingmall.shop.dto.OrderDto;
import com.example.shoppingmall.shop.dto.OrderProductDto;
import com.example.shoppingmall.shop.entity.OrderStatus;
import com.example.shoppingmall.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    // 구매 요청하기
    @PutMapping("/shop/{shopId}/product/{productId}/order/create")
    public OrderDto createOrder(
            @PathVariable("shopId") Long shopId,
            @PathVariable("productId") Long productId,
            @RequestBody OrderProductDto dto
    ) {
        return service.createOrder(shopId, productId, dto);
    }

    // 구매 요청 수락 or 거절
    @PostMapping("/shop/{shopId}/product/{productId}/order/{orderId}/accept")
    public String orderAccept(
            @PathVariable("shopId") Long shopId,
            @PathVariable("productId") Long productId,
            @PathVariable("orderId") Long orderId,
            @RequestParam boolean flag
    ) {
        OrderStatus status = service.accept(shopId, productId, orderId, flag);
        return "Order status: " + status;
    }

    // 구매 취소 요청
    @PostMapping("/{orderId}/cancel")
    public String cancelRequest(@PathVariable("orderId") Long id) {
        OrderStatus status = service.cancelRequest(id);
        return "Order status: " + status;
    }
}
