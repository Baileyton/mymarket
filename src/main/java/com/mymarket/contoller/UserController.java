package com.mymarket.contoller;

import com.mymarket.dto.*;
import com.mymarket.entity.User;
import com.mymarket.jwt.JwtUtil;
import com.mymarket.security.UserDetailsImpl;
import com.mymarket.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequestDto requestDto) {
        try {
            User signupUser = userService.signUp(requestDto);
            return ResponseEntity.ok(signupUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/detail")
    public ResponseEntity<?> getDetail(@PathVariable("userId") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long loggedInUserId = userDetails.getUser().getId();

        if (loggedInUserId != id) {
            return ResponseEntity.status(403).body("You are not authorized to view this user's details.");
        }

        try {
            User user = userService.getDetail(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // 클라이언트 측에서 JWT 토큰을 삭제하도록 요청
        return ResponseEntity.ok("Successfully logged out");
    }
}
