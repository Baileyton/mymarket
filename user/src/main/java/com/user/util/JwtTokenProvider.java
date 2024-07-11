package com.user.util;

import com.user.entity.UserRoleEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    public static final String AUTHORIZATION_KEY = "auth";
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String email, UserRoleEnum role) {
        Date date = new Date();

        // Redis에 토큰 저장 및 만료 시간 설정

        String token = Jwts.builder()
                .setSubject(email) // 사용자 식별자값(ID)
                .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();

        String redisKey = "JWT_TOKEN:" + email;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(redisKey, token, TOKEN_TIME, TimeUnit.MILLISECONDS);

        return token;
    }

}