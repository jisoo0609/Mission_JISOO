package com.example.shoppingmall.used;

import com.example.shoppingmall.used.dto.ItemDto;
import com.example.shoppingmall.used.dto.ProposalDto;
import com.example.shoppingmall.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/used")
@RequiredArgsConstructor
public class UsedController {
    private final UsedService service;

    // 중고거래 물건 등록
    @PutMapping("/create")
    public ItemDto createItem(
            @RequestBody ItemDto dto
    ) {
        return service.crateItem(dto);
    }

    // 물건 조회
    @GetMapping("/{id}")
    public ItemDto readOne(@PathVariable("id") Long id) {
        return service.readOne(id);
    }

    // 중고거래 물건 img 추가
    @PostMapping("/{id}/update-image")
    public ItemDto updateItemImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile imageFile) {
        return service.updateItemImage(id, imageFile);
    }

    // 중고거래 물품 수정
    @PostMapping ("/{id}/update")
    public ItemDto update(
            @PathVariable("id") Long id,
            @RequestBody ItemDto dto
    ) {
        return service.update(id, dto);
    }

    // 중고거래 물품 삭제
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        service.delete(id);
        return "Delete";
    }

    // 중고거래 제안
    @PutMapping("/{id}/proposal")
    public ProposalDto proposal(@PathVariable("id") Long id) {
        return service.createProposal(id);
    }
}
