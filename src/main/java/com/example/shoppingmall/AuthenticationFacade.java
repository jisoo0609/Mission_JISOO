package com.example.shoppingmall;

import com.example.shoppingmall.entity.CustomUserDetails;
import com.example.shoppingmall.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
    private JpaUserDetailsManager manager;
    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 현재 사용자가 관리자인지 확인
    public boolean isCurrentAdmin() {
        Authentication authentication = getAuth();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }
}
