package com.user.controller;

import com.user.dto.PasswordUpdateRequestDto;
import com.user.dto.SignupRequestDto;
import com.user.dto.UpdateProfileRequestDto;
import com.user.entity.User;
import com.user.security.UserDetailsImpl;
import com.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
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
        return ResponseEntity.ok(userService.signUp(requestDto));
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getDetail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        long loggedInUserId = userDetails.getUser().getId();
        return ResponseEntity.ok(userService.getDetail(loggedInUserId));
    }


    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody PasswordUpdateRequestDto requestDto) {
        userService.updatePassword(userDetails.getUser(), requestDto);
        return ResponseEntity.ok("비밀번호 변경을 성공했습니다.");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @Valid @RequestBody UpdateProfileRequestDto requestDto) {
        userService.updateProfile(userDetails.getUser(), requestDto);
        return ResponseEntity.ok("주소, 휴대폰 번호 업데이트 성공했습니다.");
    }

}