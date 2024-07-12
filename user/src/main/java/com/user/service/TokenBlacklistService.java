package com.user.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {
    private Set<String> tokenBlacklist = new HashSet<>();

    // 블랙리스트에 토큰 추가
    public void addToBlacklist(String token) {
        tokenBlacklist.add(token);
    }

    // 블랙리스트에서 토큰 삭제
    public void removeFromBlacklist(String token) {
        tokenBlacklist.remove(token);
    }

    // 블랙리스트에 토큰이 있는지 확인
    public boolean isBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}
