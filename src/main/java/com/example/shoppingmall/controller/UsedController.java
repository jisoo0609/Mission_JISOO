package com.example.shoppingmall.controller;

import com.example.shoppingmall.used.dto.ItemDto;
import com.example.shoppingmall.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/used")
@RequiredArgsConstructor
public class UsedController {
    private final UsedService service;

    // 중고거래 물건 등록
    @RequestMapping("/create")
    public String createItem(
            @RequestBody ItemDto dto
    ) {
        service.crateItem(dto);
        return "REGISTER";
    }

    // 중고거래 물건 img 추가
    @RequestMapping("/{id}/update-image")
    public String updateImage(@PathVariable("id") Long id) {
        return "Img Update";
    }

    // 중고거래 물품 수정
    @RequestMapping("/{id}/update")
    public String update(@PathVariable("id") Long id) {
        return "Update";
    }

    // 중고거래 물품 삭제
    @RequestMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        return "Delete";
    }

    // 중고거래 제안
    @RequestMapping("/{id}/proposal")
    public String proposal(@PathVariable("id") Long id) {
        return "SUGGEST";
    }
}
