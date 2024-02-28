package com.example.shoppingmall.used.entity;

import com.example.shoppingmall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Setter
    private ItemStatus status;

    @ManyToOne
    private UserEntity user;    // 제안한 User

    @ManyToOne
    private Item item;  // item
}
