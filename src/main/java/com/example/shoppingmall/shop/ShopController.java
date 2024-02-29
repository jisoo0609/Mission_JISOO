package com.example.shoppingmall.shop;

import com.example.shoppingmall.shop.dto.ShopDto;
import com.example.shoppingmall.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;

    // 쇼핑몰 생성하기
    @PutMapping("/create")
    public ShopDto createShop(@RequestBody ShopDto dto) {
        return service.crateShop(dto);
    }

    // 쇼핑몰 정보 불러오기
    @GetMapping("{id}")
    public ShopDto readOne(@PathVariable Long id) {
        return service.readOne(id);
    }

    // 쇼핑몰 정보 수정
    @PostMapping("/{id}/update")
    public ShopDto updateShop(@PathVariable Long id, @RequestBody ShopDto dto) {
        return service.updateShop(id, dto);
    }

}
