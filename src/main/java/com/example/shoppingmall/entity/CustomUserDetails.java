package com.example.shoppingmall.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    @Getter
    private Long id;
    private String username;
    private String password;

    private String name;    // 사용자 이름
    private String nickname;    // 닉네임
    private Integer age;    // 연령대
    private String email;   // 이메일
    private String phone;   // 전화번호

    private String authorities;     // 권한

    public String getRawAuthorities() {
        return this.authorities;
    }

    @Override
    // 권한 부여
    // 비활성 사용자, 일반 사용자, 사업자 사용자, 관리자
    // 권한: ROLE_INACTIVE_USER, ROLE_USER, ROLE_BUSINESS_USER, ROLE_ADMINISTRATOR
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(","))
                .sorted()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
