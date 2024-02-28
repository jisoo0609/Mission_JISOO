package com.example.shoppingmall.used.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.used.dto.ItemDto;
import com.example.shoppingmall.used.entity.Item;
import com.example.shoppingmall.used.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsedService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authFacade;

    // 물품 등록
    public void crateItem(ItemDto dto) {
        String name = authFacade.getAuth().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(name);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        log.info("name: {}", name);
        log.info("Optional UserId: {}", optionalUser.get().getId());

        // 비활성 유저가 아니면 중고거래 가능
        if (authFacade.isInactiveUser()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserEntity user = optionalUser.get();
        dto.setStatus("판매중");
        Item newItem = Item.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .user(user)
                .build();

        log.info("newItem: {}", newItem);
        itemRepository.save(newItem);
    }
}
