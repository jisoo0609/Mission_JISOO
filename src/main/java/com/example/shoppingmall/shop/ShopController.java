package com.example.shoppingmall.shop;

import com.example.shoppingmall.shop.dto.ShopDto;
import com.example.shoppingmall.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;

    @PutMapping("/create")
    public ShopDto createShop(@RequestBody ShopDto dto) {
        return service.crateShop(dto);
    }
}
