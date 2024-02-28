package com.example.shoppingmall.used.repo;

import com.example.shoppingmall.used.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByItemId(Long id);
    List<Proposal> findByUserId(Long id);
}
