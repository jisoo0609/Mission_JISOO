package com.example.shoppingmall.shop.service;

import com.example.shoppingmall.shop.dto.OrderProductDto;
import com.example.shoppingmall.shop.dto.PaymentCancelDto;
import com.example.shoppingmall.shop.dto.PaymentConfirmDto;
import com.example.shoppingmall.shop.entity.OrderProduct;
import com.example.shoppingmall.shop.entity.OrderStatus;
import com.example.shoppingmall.shop.entity.Product;
import com.example.shoppingmall.shop.repo.OrderProductRepository;
import com.example.shoppingmall.shop.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossService {
    private final TossHttpService tossHttpService;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    // 결제 승인
    public Object confirmPayment(PaymentConfirmDto dto) {
        // HTTP 요청 보냄
        Object tossPaymentObj = tossHttpService.confirmPayment(dto);
        log.info(tossPaymentObj.toString());

        String orderName = ((LinkedHashMap<String, Object>) tossPaymentObj)
                .get("orderName").toString();

        Long productId = Long.parseLong(orderName.split("-")[0]);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        return OrderProductDto.fromEntity(orderProductRepository.save(OrderProduct.builder()
                .product(product)
                .tossPaymentKey(dto.getPaymentKey())
                .tossOrderId(dto.getOrderId())
                .status(OrderStatus.COMPLETE)
                .build()));
    }

    // readAll
    public List<OrderProductDto> readAll() {
        return orderProductRepository.findAll().stream()
                .map(OrderProductDto::fromEntity)
                .toList();
    }

    // readOne
    public OrderProductDto readOne(Long id) {
        return orderProductRepository.findById(id)
                .map(OrderProductDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // readTossPayment
    public Object readTossPayment(Long id) {
        // 주문정보 조회
        OrderProduct order = orderProductRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // paymentKey 바탕으로 Toss에 요청을 보내 결제 정보 받음
        Object response = tossHttpService.getPayment(order.getTossPaymentKey());
        log.info(response.toString());

        // 결제정보 반환
        return response;
    }

    // canclePayment
    @Transactional
    public Object canclePayment(Long id, PaymentCancelDto dto) {
        OrderProduct order = orderProductRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 주문정보 갱신
        order.setStatus(OrderStatus.CANCEL);

        // 취소 후 결과 응답
        return tossHttpService.cancelPayment(order.getTossPaymentKey(), dto);
    }

}
