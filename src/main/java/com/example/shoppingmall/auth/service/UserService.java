package com.example.shoppingmall.auth.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.CustomUserDetails;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JpaUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void create(UserDto dto) {
        dto.setAuthorities("ROLE_INACTIVE_USER");
        UserDetails newUser = buildUserDetails(dto);
        manager.createUser(newUser);
    }

    public void update(Long id, UserDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        UserEntity target = optionalUser.get();
        target.setName(dto.getName());
        target.setNickname(dto.getNickname());
        target.setAge(dto.getAge());
        target.setEmail(dto.getEmail());
        target.setPhone(dto.getPhone());

        dto.setAuthorities("ROLE_USER");
        UserDetails updatedUser = buildUserDetails(dto);
        manager.updateUser(updatedUser);
    }


    // USER가 BusinessNumber를 추가
    // 사업자로 권한 변경 신청
    public void requestBusiness(Long id, UserDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        UserEntity target = optionalUser.get();
        target.setBusinessNumber(dto.getBusinessNumber());

        userRepository.save(target);
    }

    @Transactional
    // 관리자가 신청을 수락하거나 거절함
    public void businessUpdate(boolean accept) {
        List<UserEntity> list = userRepository.findByBusinessNumberIsNotNull();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (accept) {
            for (UserEntity user : list) {
                user.setAuthorities("ROLE_BUSINESS_USER");
                userRepository.save(user);
            }
        }
    }


    // 관리자가 사용자 전환 신청 목록 확인
    public List<UserEntity> checkRequestList() {
        List<UserEntity> requestList = userRepository.findByBusinessNumberIsNotNull();
        if (requestList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return requestList;
    }

    // UserDto를 UserDetails로 변환하는 메서드
    private UserDetails buildUserDetails(UserDto dto) {
        return CustomUserDetails.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // 패스워드 인코딩 필요
                .name(dto.getName())
                .nickname(dto.getNickname())
                .age(dto.getAge())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .authorities(dto.getAuthorities())
                .build();
    }
}
