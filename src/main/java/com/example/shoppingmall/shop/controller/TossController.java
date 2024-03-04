package com.example.shoppingmall.shop.controller;

import com.example.shoppingmall.shop.dto.PaymentConfirmDto;
import com.example.shoppingmall.shop.service.TossService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/toss")
@RequiredArgsConstructor
public class TossController {
    private final TossService service;
    private final ResourceLoader resourceLoader;

    // 결제 창 보여줌
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String root() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:checkout.html");
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
}