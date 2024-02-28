package com.example.shoppingmall.used.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.jwt.JwtTokenUtils;
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

    // 물품 정보 조회
    public ItemDto readOne(Long id) {
        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    // 물품 수정
    public void update(Long id, ItemDto dto) {
        // 수정할 물건 가져옴
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Item target = optionalItem.get();

        log.info("register User: {}", target.getUser().getUsername());
        log.info("auth User: {}", authFacade.getAuthName());
        if (!target.getUser().getUsername().equals(authFacade.getAuthName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "물건을 등록한 유저만 수정할 수 있습니다.");
        }

        target.setTitle(dto.getTitle());
        target.setDescription(dto.getDescription());
        target.setPrice(dto.getPrice());

        log.info("update Item: {}", target);
        itemRepository.save(target);
    }

    // 물품 삭제
    public void delete(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Item target = optionalItem.get();

        log.info("register User: {}", target.getUser().getUsername());
        log.info("auth User: {}", authFacade.getAuthName());
        if (!target.getUser().getUsername().equals(authFacade.getAuthName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "물건을 등록한 유저만 삭제할 수 있습니다.");
        }

        itemRepository.delete(target);
    }
}
