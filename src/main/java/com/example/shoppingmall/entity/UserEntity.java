package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "user_table")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // ID

    @Column(nullable = false, unique = true)
    private String username;    // 고유 값:: id
    private String password;    // 비밀번호

    private String name;    // 사용자 이름
    private String nickname;    // 닉네임
    private Integer age;    // 연령대
    @Column(nullable = false, unique = true)
    private String email;   // 이메일
    private String phone;   // 전화번호

    // 테스트를 위해서 문자열 하나에 ','로 구분해 권한을 묘사
    // ROLE_USER,ROLE_ADMIN,READ_AUTHORITY,WRITE_AUTHORITY
    private String authorities;
}

