package com.example.shoppingmall.used.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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
    @Setter
    private String title;   // 제목
    @Setter
    private String description; // 설명
    @Setter
    private String image;   // 대표 이미지
    @Setter
    private Integer price;  // 최소 가격
    @Enumerated(EnumType.STRING)
    @Setter
    private ItemStatus status;   // 판매 상태

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "INT")
    private UserEntity user;    // 등록한 User

    @OneToMany(mappedBy = "item")
    private List<Proposal> proposal;
}
