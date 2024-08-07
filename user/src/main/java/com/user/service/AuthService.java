package com.user.service;

import com.core.global.dto.TokenResponseDto;
import com.core.global.exception.BadRequestException;
import com.core.global.jwt.JwtTokenProvider;
import com.core.user.dto.UserDto;
import com.core.user.entity.User;
import com.core.user.repository.UserRepository;
import com.user.dto.LoginDto;
import com.user.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Transactional
    public void join(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BadRequestException("이미 가입되어있는 이메일 입니다.");
        }
        User user = registerDto.toEntity();
        user.hashPassword(bCryptPasswordEncoder);
        userRepository.save(user);
    }

    @Transactional
    public TokenResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(
                () -> new BadRequestException("이메일을 확인하세요.")
        );
        user.checkPassword(loginDto.getPassword(), bCryptPasswordEncoder);

        //로그인 성공시 jwt 토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail());

        return new TokenResponseDto(token);
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BadRequestException("유저 정보를 찾을 수 없습니다.")
        );
        //Token에서 로그인한 사용자 정보 get해 로그아웃 처리
        String key = "JWT_TOKEN:" + user.getEmail();
        if (redisTemplate.opsForValue().get(key) != null) {
            redisTemplate.delete(key); // Token 삭제
        }
    }

    @Transactional(readOnly = true)
    public UserDto getAuth(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BadRequestException("유저 정보를 찾을 수 없습니다.")
        );
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }
}