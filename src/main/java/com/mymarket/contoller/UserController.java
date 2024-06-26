package com.mymarket.contoller;

import com.mymarket.dto.LoginRequestDto;
import com.mymarket.dto.SignupRequestDto;
import com.mymarket.dto.UserInfoDto;
import com.mymarket.entity.User;
import com.mymarket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto) {
//        return ResponseEntity.ok(userService.login(requestDto));
//    }
}
