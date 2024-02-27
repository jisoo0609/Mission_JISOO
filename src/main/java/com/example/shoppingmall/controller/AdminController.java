package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.UserDto;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService service;

    // 관리자가 사용자 전환 신청 목록 확인
    @GetMapping("/list")
    public List<UserEntity> checkRequestList() {
        return service.checkRequestList();
    }
}
