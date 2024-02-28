package com.example.shoppingmall.controller;

import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.jwt.dto.JwtRequestDto;
import com.example.shoppingmall.jwt.dto.JwtResponseDto;
import com.example.shoppingmall.jwt.JwtTokenUtils;
import com.example.shoppingmall.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    // login
    @PostMapping("/login")
    public JwtResponseDto issueJwt(@RequestBody JwtRequestDto dto) {
        // 사용자가 제공한 username(id), password가 저장된 사용자인지 판단
        if (!manager.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        UserDetails userDetails
                = manager.loadUserByUsername(dto.getUsername());

        // 비밀번호 확인
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // JWT 발급
        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }

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
