package com.example.shoppingmall.used.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.used.dto.ItemDto;
import com.example.shoppingmall.used.dto.ProposalDto;
import com.example.shoppingmall.used.entity.Item;
import com.example.shoppingmall.used.entity.ItemStatus;
import com.example.shoppingmall.used.entity.Proposal;
import com.example.shoppingmall.used.repo.ItemRepository;
import com.example.shoppingmall.used.repo.ProposalRepository;
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
    private final ProposalRepository proposalRepository;
    private final AuthenticationFacade authFacade;

    // 물품 등록
    public ItemDto crateItem(ItemDto dto) {
        String name = authFacade.getAuth().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(name);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        log.info("name: {}", name);
        log.info("Optional UserId: {}", optionalUser.get().getId());

        UserEntity user = optionalUser.get();
        Item newItem = Item.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .status(ItemStatus.SALE)
                .user(user)
                .build();

        log.info("newItem: {}", newItem);
        return ItemDto.fromEntity(itemRepository.save(newItem));
    }

    // 물품 정보 조회
    public ItemDto readOne(Long id) {
        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    // 물품 수정
    public ItemDto update(Long id, ItemDto dto) {
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
        return ItemDto.fromEntity(itemRepository.save(target));
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

    // 구매 제안
    public ProposalDto createProposal(Long id) {
        // 구매 제안을 하는 유저 정보
        String name = authFacade.getAuthName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(name);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 구매 제안할 아이템 가져옴
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Item item = optionalItem.get();

        log.info("register User: {}", item.getUser().getUsername());
        log.info("auth User: {}", name);
        if (item.getUser().getUsername().equals(name)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "자신의 등록한 물건에 거래제안을 할 수 없습니다.");
        }

        // 구매 요청하는 자와 물품을 등록한 사용자가 같지 않으면 제안 가능
        Proposal newProposal = Proposal.builder()
                .status(item.getStatus())
                .user(optionalUser.get())
                .item(item)
                .build();

        return ProposalDto.fromEntity(proposalRepository.save(newProposal));
    }
}
