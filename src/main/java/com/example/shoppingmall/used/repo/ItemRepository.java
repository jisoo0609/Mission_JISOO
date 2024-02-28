package com.example.shoppingmall.used.repo;

import com.example.shoppingmall.used.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
