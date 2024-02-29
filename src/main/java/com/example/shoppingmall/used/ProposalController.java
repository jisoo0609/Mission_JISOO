package com.example.shoppingmall.used;

import com.example.shoppingmall.used.dto.ProposalDto;
import com.example.shoppingmall.used.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("proposal")
@RequiredArgsConstructor
public class ProposalController {
    private final ProposalService service;

    // 구매 제안 확인
    @GetMapping
    public List<ProposalDto> readAll() {
        return service.readAll();
    }

    // 제안 전체 확인
    @GetMapping("/{id}")
    public List<ProposalDto> readAll(@PathVariable("id") Long id) {
        return service.readAll(id);
    }

    // 구매 제안 수락 또는 거절
    @PostMapping("/{id}/accept")
    public String accept(
            @PathVariable("id") Long id,
            @RequestParam boolean accept
    ) {
        service.accept(id, accept);
        if (accept) {
            return "ACCEPT";
        }
        return "REJECT";
    }

    @PostMapping("/{id}/confirm")
    public String confirm(
            @PathVariable("id") Long id,
            @RequestParam boolean confirm
    ) {
        service.confirm(id, confirm);
        return "CONFIRM";
    }

}
