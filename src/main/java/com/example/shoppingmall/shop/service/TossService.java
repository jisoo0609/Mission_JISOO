package com.example.shoppingmall.shop.service;

import com.example.shoppingmall.shop.dto.PaymentConfirmDto;
import com.example.shoppingmall.shop.repo.OrderRepository;
import com.example.shoppingmall.used.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossService {
    private final TossHttpService tossHttpService;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    // 결제 승인
    public Object confirmPayment(PaymentConfirmDto dto) {
        // HTTP 요청 보냄
        Object tossPaymentObj = tossHttpService.confirmPayment(dto);
        log.info(tossPaymentObj.toString());
        return tossPaymentObj;
    }

}
