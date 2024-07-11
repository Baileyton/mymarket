package com.user.service;

import com.user.dto.*;
import com.user.entity.User;
import com.user.entity.UserRoleEnum;
import com.user.exception.ErrorCode;
import com.user.exception.UserException;
import com.user.repository.UserRepository;
import com.user.util.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Transactional // 메서드 수준에서 트랜잭션 적용
    public User signUp(SignupRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new UserException(ErrorCode.EXIST_USER);
        }

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(encodeField(requestDto.getPassword()))
                .phone(encodeField(requestDto.getPhone()))
                .address(encodeField(requestDto.getAddress()))
                .role(UserRoleEnum.USER) // 기본 역할 설정
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new UserException(ErrorCode.INCORRECT_PASSWORD);
        }

        UserRoleEnum role = user.getRole();

        String token = jwtTokenProvider.createToken(requestDto.getEmail(), role);
        return new TokenResponseDto(token);
    }

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public User getDetail(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    public void updatePassword(User user, PasswordUpdateRequestDto requestDto) {
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new UserException(ErrorCode.INCORRECT_PASSWORD);
        }

        String newEncodedPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(newEncodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void updateProfile(User user, UpdateProfileRequestDto requestDto) {
        user.updateAddress(passwordEncoder.encode(requestDto.getNewAddress()));
        user.updatePhone(passwordEncoder.encode(requestDto.getNewPhone()));
        userRepository.save(user);
    }

    private String encodeField(String field) {
        return passwordEncoder.encode(field);
    }
}