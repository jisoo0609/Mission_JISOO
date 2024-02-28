package com.example.shoppingmall.used.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.used.dto.ProposalDto;
import com.example.shoppingmall.used.entity.Item;
import com.example.shoppingmall.used.entity.ItemStatus;
import com.example.shoppingmall.used.entity.Proposal;
import com.example.shoppingmall.used.repo.ItemRepository;
import com.example.shoppingmall.used.repo.ProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;
    private final AuthenticationFacade authFacade;

    // 구매 제안 조회 - Item을 등록한 유저
    public List<ProposalDto> readAll(Long id) {
        // 구매 제안 정보 조회할 아이템 가져옴
        Item item = getItem(id);

        // item을 등록한 사용자와 페이지에 접근한 사용자가 일치하는 경우
        log.info("register User: {}", item.getUser().getUsername());
        log.info("auth User: {}", authFacade.getAuthName());
        List<Proposal> proposalList;
        if (!item.getUser().getUsername().equals(authFacade.getAuthName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // item의 Id를 기준으로 해당하는 구매 제안 불러옴
        proposalList = proposalRepository.findByItemId(item.getId());
        return proposalList.stream()
                .map(proposal -> ProposalDto.builder()
                        .id(proposal.getId())
                        .status(proposal.getItem().getStatus())
                        .userId(proposal.getUser().getId())
                        .itemId(proposal.getItem().getId())
                        .build())
                .collect(Collectors.toList());
    }

    // 구매 제안 조회 - 제안한 유저
    public List<ProposalDto> readAll() {
        UserEntity user = getUserEntity();
        List<Proposal> proposalList = proposalRepository.findByUserId(user.getId());
        return proposalList.stream()
                .map(proposal -> ProposalDto.builder()
                        .id(proposal.getId())
                        .status(proposal.getItem().getStatus())
                        .userId(proposal.getUser().getId())
                        .itemId(proposal.getItem().getId())
                        .build())
                .collect(Collectors.toList());
    }

    // 구매 제안 수락 또는 거절
    // 물품을 등록한 사용자가 구매 제안을 수락 또는 거절 가능하다.
    @Transactional
    public void accept(Long id, boolean flag) {
        Optional<Proposal> optionalProposal = proposalRepository.findById(id);
        if (optionalProposal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Proposal proposal = optionalProposal.get();

        String user = proposal.getItem().getUser().getUsername();
        // proposal의 item의 user_id와 현재 접근한 유저가 일치하는지 확인
        log.info("item Owner: {}", user);
        log.info("auth User: {}", authFacade.getAuthName());
        if (user.equals(authFacade.getAuthName())) {
            if (flag) {
                proposal.setStatus(ItemStatus.ACCEPT);
                proposal.getItem().setStatus(ItemStatus.ACCEPT);
            } else {
                proposal.setStatus(ItemStatus.REJECT);
                proposal.getItem().setStatus(ItemStatus.REJECT);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    // 구매 확정
    // 제안을 등록한 사용자가 구매 확정 가능하다.
    @Transactional
    public void confirm(Long id, boolean confirm) {
        Optional<Proposal> optionalProposal = proposalRepository.findById(id);
        if (optionalProposal.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Proposal proposal = optionalProposal.get();

        String user = proposal.getUser().getUsername();
        // proposal의 user와 현재 접근한 유저가 일치하는지 확인
        log.info("proposal user: {}", user);
        log.info("auth User: {}", authFacade.getAuthName());
        if (user.equals(authFacade.getAuthName())) {
            // 불러온 구매 제안이 수락인 경우
            if (proposal.getStatus().equals(ItemStatus.ACCEPT) && confirm) {
                log.info("original Status: {}", proposal.getStatus());
                // 구매 확정으로 제안 상태 변경
                proposal.setStatus(ItemStatus.CONFIRM);
                log.info("confirm Status: {}", proposal.getStatus());
                // 구매 제안이 확정일 경우 물품의 상태 판매 완료로 변경
                proposal.getItem().setStatus(ItemStatus.SOLD);
                log.info("sold out: {}", proposal.getItem().getStatus());

                // item_id 불러옴
                Long itemId = proposal.getItem().getId();
                // 해당 id와 같은 요청 전부 거절로 변경
                List<Proposal> proposalList = proposalRepository.findByItemId(itemId);
                for (Proposal p : proposalList) {
                    if (!p.getId().equals(proposal.getId())) {
                        p.setStatus(ItemStatus.REJECT);
                    }
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private UserEntity getUserEntity() {
        String authName = authFacade.getAuthName();
        return userRepository.findByUsername(authName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
