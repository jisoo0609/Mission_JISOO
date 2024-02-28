package com.example.shoppingmall.controller;

import com.example.shoppingmall.used.dto.ProposalDto;
import com.example.shoppingmall.used.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("proposal")
@RequiredArgsConstructor
public class ProposalController {
    private final ProposalService service;

    @GetMapping
    public List<ProposalDto> readAll() {
        return service.readAll();
    }

    // 제안 전체 확인
    @GetMapping("/{id}")
    public List<ProposalDto> readAll(@PathVariable("id") Long id) {
        return service.readAll(id);
    }


}
