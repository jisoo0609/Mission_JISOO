package com.example.shoppingmall.service;

import com.example.shoppingmall.AuthenticationFacade;
import com.example.shoppingmall.entity.CustomUserDetails;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager{
    private final UserRepository userRepository;

    public JpaUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        // 관리자
        createUser(CustomUserDetails.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .authorities("ROLE_ADMIN")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        UserEntity userEntity = optionalUser.get();
        return CustomUserDetails.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .build();
    }

    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            UserEntity newUser;
            if (userDetails.getUsername().equals("admin")) { // 관리자 계정인 경우
                newUser = UserEntity.builder()
                        .username(userDetails.getUsername())
                        .password(userDetails.getPassword())
                        .authorities("ROLE_ADMIN") // 관리자 권한 지정
                        .build();
            } else { // 일반 사용자인 경우
                newUser = UserEntity.builder()
                        .username(userDetails.getUsername())
                        .password(userDetails.getPassword())
                        .authorities("ROLE_INACTIVE_USER") // 기본적으로 비활성 사용자 권한 지정
                        .build();
            }
            userRepository.save(newUser);
        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        // userName으로 user 가져옴
        Optional<UserEntity> optionalUser = userRepository.findByUsername(user.getUsername());
        // user가 없으면 예외 발생
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(user.getUsername());

        CustomUserDetails userDetails = (CustomUserDetails) user;
        // UPDATE
        UserEntity target = optionalUser.get();
        target.setName(userDetails.getName());
        target.setNickname(userDetails.getNickname());
        target.setAge(userDetails.getAge());
        target.setEmail(userDetails.getEmail());
        target.setPhone(userDetails.getPhone());
        // 권한 업데이트
        target.setAuthorities("ROLE_USER");
        userRepository.save(target);
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }


}

