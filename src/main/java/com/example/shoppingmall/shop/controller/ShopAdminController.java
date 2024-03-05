package com.example.shoppingmall.shop.controller;

import com.example.shoppingmall.shop.dto.ShopDto;
import com.example.shoppingmall.shop.entity.ShopStatus;
import com.example.shoppingmall.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/admin")
@RequiredArgsConstructor
public class ShopAdminController {
    private final ShopService service;

    // 개설 신청된 쇼핑몰 목록 확인
    @GetMapping
    public List<ShopDto> openRequestShopList() {
        return service.openRequestShopList();
    }

    // 쇼핑몰 개설 허가 / 불허
    @PostMapping("/{id}/accept")
    public String accept(
            @PathVariable("id") Long id,
            @RequestParam boolean flag,
            @RequestBody(required = false) ShopDto dto
    ) {
        ShopStatus status = service.accept(id, flag, dto);
        return "Shop status: " + status;
    }

    // 관리자가 폐쇄 수락
    @PostMapping("/{id}/close-accept")
    public String closeAccept(
            @PathVariable("id") Long id,
            @RequestParam boolean flag
    ) {
       ShopStatus status = service.closeAccept(id, flag);
       return "Shop status: "+ status;
    }
}
