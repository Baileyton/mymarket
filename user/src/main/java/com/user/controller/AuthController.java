package com.user.controller;

import com.core.global.dto.TokenResponseDto;
import com.core.global.util.AuthenticationUtils;
import com.core.user.dto.UserDto;
import com.user.dto.LoginDto;
import com.user.dto.RegisterDto;
import com.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/join")
    public void join(@Valid @RequestBody RegisterDto registerDto) {
        authService.join(registerDto);
    }

    @PostMapping("/login")
    public TokenResponseDto login(@Valid @RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/logout")
    public void logout() {
        authService.logout(AuthenticationUtils.getUserIdByToken());
    }

    @GetMapping
    public UserDto getAuth() {
        return authService.getAuth(AuthenticationUtils.getUserIdByToken());
    }
}