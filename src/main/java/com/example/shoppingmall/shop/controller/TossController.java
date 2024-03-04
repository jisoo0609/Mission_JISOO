package com.example.shoppingmall.shop.controller;

import com.example.shoppingmall.shop.dto.OrderProductDto;
import com.example.shoppingmall.shop.dto.PaymentCancelDto;
import com.example.shoppingmall.shop.dto.PaymentConfirmDto;
import com.example.shoppingmall.shop.service.TossService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/toss")
@RequiredArgsConstructor
public class TossController {
    private final TossService service;
    private final ResourceLoader resourceLoader;

    // 결제 창 보여줌 - view
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String root() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/checkout.html");
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @PostMapping("/confirm-payment")
    public Object confirmPayment(
            @RequestBody
            PaymentConfirmDto dto
    ) {
        log.info("received: {}", dto.toString());
        return service.confirmPayment(dto);
    }

    @GetMapping("/readAll")
    public List<OrderProductDto> readAll() {
        return service.readAll();
    }

    @GetMapping("/{id}")
    public OrderProductDto readOne(@PathVariable("id") Long id) {
        return service.readOne(id);
    }

    // 결제 정보 가져오기
    @GetMapping("/{id}/payment")
    public Object readTossPayment(@PathVariable("id") Long id) {
        return service.readTossPayment(id);
    }

    // 결제 취소
    @PostMapping("/{id}/cancel")
    public Object cancelPayment(@PathVariable("id") Long id, @RequestBody PaymentCancelDto dto) {
        return service.canclePayment(id, dto);
    }
}