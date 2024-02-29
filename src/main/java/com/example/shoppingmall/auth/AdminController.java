package com.example.shoppingmall.auth;

import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @PostMapping("/check")
    public String businessUpdate(@RequestParam boolean accept) {
        service.businessUpdate(accept);
        if (accept) {
            return "UPGRADE ACCEPT";
        } else {
            return "REJECT";
        }
    }
}
