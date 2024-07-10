package com.user.controller;

import com.user.dto.LoginRequestDto;
import com.user.dto.SignupRequestDto;
import com.user.dto.SignupResponseDto;
import com.user.dto.TokenResponseDto;
import com.user.entity.User;
import com.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signUp(@Valid @RequestBody SignupRequestDto requestDto) {
        User user = userService.signUp(requestDto);
        SignupResponseDto responseDto = new SignupResponseDto(user.getName(), user.getEmail(), "회원가입 성공");
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/login")
    public TokenResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        return userService.login(requestDto);
    }

//    @GetMapping("/detail")
//    public ResponseEntity<?> getDetail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        long loggedInUserId = userDetails.getUser().getId();
//        return ResponseEntity.ok(userService.getDetail(loggedInUserId));
//    }
//
//
//    @PutMapping("/password")
//    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                 @Valid @RequestBody PasswordUpdateRequestDto requestDto) {
//        userService.updatePassword(userDetails.getUser(), requestDto);
//        return ResponseEntity.ok("비밀번호 변경을 성공했습니다.");
//    }
//
//    @PutMapping("/profile")
//    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                @Valid @RequestBody UpdateProfileRequestDto requestDto) {
//        userService.updateProfile(userDetails.getUser(), requestDto);
//        return ResponseEntity.ok("주소, 휴대폰 번호 업데이트 성공했습니다.");
//    }

}