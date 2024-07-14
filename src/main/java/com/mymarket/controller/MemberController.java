package com.mymarket.controller;

import com.mymarket.dto.*;
import com.mymarket.entity.Member;
import com.mymarket.service.MemberService;
import com.mymarket.service.TokenService;
import com.mymarket.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDto) {
        try {
            // signup으로 들어오는 모든 user는 기본 회원이라는 가정으로 권한 부여
            memberDto.setRole("ROLE_USER");
            // 회원 정보 저장
            Member member = memberService.registerUser(memberDto);

            // 저장 성공 시 응답
            String welcomeMessage = member.getEmail();
            return ResponseEntity.ok(welcomeMessage);
        } catch (IllegalArgumentException e) {
            // 예외 발생 시 BadRequest 응답
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        return memberService.login(request, response);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String accessToken, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        memberService.updatePassword(accessToken, updatePasswordDTO);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestHeader("Authorization")String accessToken, @RequestBody UpdateProfileDTO updateProfileDTO) {
        memberService.updateProfile(accessToken, updateProfileDTO);
        return ResponseEntity.ok("Profile updated successfully");
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken) {
        memberService.logout(accessToken);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/refresh-token")
    public void refreshToken(@RequestHeader("Authorization") String accessToken, @RequestHeader("RefreshToken") String refreshToken, HttpServletResponse res) {
        String token = accessToken.replace(JwtUtil.BEARER_PREFIX, "");
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();

        if (!jwtUtil.validateToken(token) && tokenService.getRefreshToken(username).equals(refreshToken) && jwtUtil.validateToken(refreshToken)) {
            String newAccessToken = jwtUtil.createAccessToken(username, jwtUtil.getUserInfoFromToken(token).get(JwtUtil.AUTHORIZATION_KEY).toString());
            String newRefreshToken = jwtUtil.createRefreshToken();

            tokenService.storeRefreshToken(username, newRefreshToken);

            jwtUtil.addJwtToCookie(newAccessToken, res);
            res.addHeader("RefreshToken", newRefreshToken);
        } else {
            throw new IllegalArgumentException("Invalid refresh token or access token.");
        }
    }
}
