package com.user.controller;

import com.user.dto.*;
import com.user.entity.User;
import com.user.exception.UserException;
import com.user.jwt.AuthenticationUtils;
import com.user.jwt.JwtUtil;
import com.user.security.UserDetailsImpl;
import com.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequestDto requestDto) {
        try {
            User signupUser = userService.signUp(requestDto);
            return ResponseEntity.ok(signupUser);
        } catch (UserException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getErrorMessage());
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getDetail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        long loggedInUserId = userDetails.getUser().getId();

        try {
            User user = userService.getDetail(loggedInUserId);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getErrorMessage());
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody PasswordUpdateRequestDto requestDto) {
        try {
            userService.updatePassword(userDetails.getUser(), requestDto);
            return ResponseEntity.ok("비밀번호 변경을 성공했습니다.");
        } catch (UserException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getErrorMessage());
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @Valid @RequestBody UpdateProfileRequestDto requestDto) {
        userService.updateProfile(userDetails.getUser(), requestDto);
        return ResponseEntity.ok("주소, 휴대폰 번호 업데이트 성공했습니다.");
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout(AuthenticationUtils.getUserIdByToken());
    }
}