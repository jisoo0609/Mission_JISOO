package com.example.shoppingmall.auth.service;

import com.example.shoppingmall.auth.entity.CustomUserDetails;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager{
    private final UserRepository userRepository;

    public JpaUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        if (!userExists("admin")) {
            // 관리자
            createUser(CustomUserDetails.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .authorities("ROLE_ADMIN")
                    .build());
        }
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
                .authorities(userEntity.getAuthorities())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .image(userEntity.getImage())
                .build();
    }

    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .authorities(userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")))
                    .build();
            log.info("authorities: {}", userDetails.getRawAuthorities());
            userRepository.save(newUser);
        } catch (Exception e) {
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
        target.setAuthorities(userDetails.getRawAuthorities());
        log.info("target authorities: {}", userDetails.getRawAuthorities());
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

