package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.UserDto;
import com.example.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    // 회원가입
    // USER CREATE
    @PostMapping("/register")
    public String createUser(@RequestBody UserDto dto) {
        service.create(dto);
        return "SUCCESS";
    }

    // USER UPDATE
    @PostMapping("/{id}/update")
    public String updateUser(
            @PathVariable("id") Long id, @RequestBody UserDto dto)
    {
        service.update(id, dto);
        return "UPDATE USER";
    }

    // USER가 사업자로 권한 변경 신청
    @PostMapping("/{id}/request")
    public String requestBusiness(
            @PathVariable("id") Long id, @RequestBody UserDto dto)
    {
        service.requestBusiness(id, dto);
        return "request Role Change To Business";
    }
}
