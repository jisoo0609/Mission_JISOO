package com.example.shoppingmall.retrade.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;   // 제목
    private String description; // 설명
    private String image;   // 대표 이미지
    private Integer price;  // 최소 가격
    private String status;   // 판매 상태

    @ManyToMany(mappedBy = "items")
    private List<UserEntity> users = new ArrayList<>();
}
