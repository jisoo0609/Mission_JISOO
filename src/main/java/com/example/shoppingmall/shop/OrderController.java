package com.example.shoppingmall.shop;

import com.example.shoppingmall.shop.dto.OrderDto;
import com.example.shoppingmall.shop.dto.OrderProductDto;
import com.example.shoppingmall.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop/{shopId}/product/{productId}/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    // 구매 요청하기
    @PutMapping("/create")
    public OrderDto createOrder(
            @PathVariable("shopId") Long shopId,
            @PathVariable("productId") Long productId,
            @RequestBody OrderProductDto dto
    ) {
        return service.createOrder(shopId, productId, dto);
    }
}
