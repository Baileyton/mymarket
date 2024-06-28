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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequestDto requestDto) {
        try {
            User signupUser = userService.signUp(requestDto);
            return ResponseEntity.ok(signupUser);
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

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, @RequestHeader("Authorization") String token) {
//        String jwt = jwtUtil.getJwtFromHeader(request);
//        if (jwt != null && jwtUtil.validateToken(jwt)) {
//            jwtUtil.blacklistToken(jwt);
//            SecurityContextHolder.clearContext();
//            return ResponseEntity.ok("로그아웃 성공했습니다.");
//        } else {
//            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
//        }
//    }
}
