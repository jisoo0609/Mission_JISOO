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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authFacade;

    // 회원가입
    public UserDto create(UserDto dto) {
        UserEntity newUser = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .authorities("ROLE_INACTIVE_USER")
                .build();

        return UserDto.fromEntity(userRepository.save(newUser));
    }
    
    // 필수 정보 추가
    // 일반 사용자로 업데이트
    public UserDto update(Long id, UserDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        UserEntity target = optionalUser.get();

        log.info("register User: {}",target.getUsername());
        log.info("auth User : {}", authFacade.getAuthName());
        if (!target.getUsername().equals(authFacade.getAuthName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        target.setName(dto.getName());
        target.setNickname(dto.getNickname());
        target.setAge(dto.getAge());
        target.setEmail(dto.getEmail());
        target.setPhone(dto.getPhone());

        target.setAuthorities("ROLE_USER");

        return UserDto.fromEntity(userRepository.save(target));
    }

    // USER 프로필 추가
    public UserDto updateUserImage(Long id, MultipartFile image) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        UserEntity target = optionalUser.get();
        
        log.info("register User: {}",target.getUsername());
        log.info("auth User : {}", authFacade.getAuthName());
        if (!target.getUsername().equals(authFacade.getAuthName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        // 파일 업로드 위치 정하기
        String UserDir = String.format("media/User/%d/", id);
        log.info(UserDir);

        try {
            Files.createDirectories(Path.of(UserDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 파일 이름 경로, 확장자 포함
        String originalFileName = image.getOriginalFilename();
        String[] fileNameSplit = originalFileName.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length -1];
        String userFileName = "User." + extension;
        log.info(userFileName);

        String userPath = UserDir + userFileName;
        log.info(userPath);

        // 저장
        try {
            image.transferTo(Path.of(userPath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String requestPath = String.format("/static/%d/%s", id, userFileName);
        log.info(requestPath);
        target.setImage(requestPath);

        return UserDto.fromEntity(userRepository.save(target));
    }

    // USER가 BusinessNumber를 추가
    // 사업자로 권한 변경 신청
    public UserDto requestBusiness(Long id, UserDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        UserEntity target = optionalUser.get();

        // 사용자가 일치하는지 확인
        log.info("register User: {}",target.getUsername());
        log.info("auth User : {}", authFacade.getAuthName());
        if (!target.getUsername().equals(authFacade.getAuthName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        target.setBusinessNumber(dto.getBusinessNumber());

        return UserDto.fromEntity(userRepository.save(target));
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
