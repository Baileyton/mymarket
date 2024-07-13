package com.mymarket.service;

import com.mymarket.dto.*;
import com.mymarket.entity.Member;
import com.mymarket.repository.MemberRepository;
import com.mymarket.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member registerUser(MemberDto memberDto) {
        if (memberDto.getEmail() == null || memberDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        if (memberDto.getUserName() == null || memberDto.getUserName().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (memberDto.getPassword() == null || memberDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use. Please choose another one.");
        }

        Member Member = new Member();
        Member.setEmail(memberDto.getEmail());
        Member.setUserName(passwordEncoder.encode(memberDto.getUserName()));
        Member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        Member.setRole("ROLE_USER");

        try {
            return memberRepository.save(Member);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error. Please try again.");
        }
    }

    public void updatePassword(String accessToken, UpdatePasswordDTO updatePasswordDTO) {
        String token = accessToken.replace(JwtUtil.BEARER_PREFIX, "");
        String email = jwtUtil.getUserInfoFromToken(token).getSubject(); // 토큰에서 이메일 추출
        log.info("update password for email: {}", email);

        Member Member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), Member.getPassword())) {
            throw new IllegalArgumentException("Invalid current password.");
        }

        Member.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        memberRepository.save(Member);
    }

    public void updateProfile(String accessToken, UpdateProfileDTO updateProfileDTO) {
        String token = accessToken.replace(JwtUtil.BEARER_PREFIX, "");
        String email = jwtUtil.getUserInfoFromToken(token).getSubject();
        log.info("update profile for email: {}", email);

        Member Member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("User not found!")
        );
        if (updateProfileDTO.getAddress() != null) {
            Member.setAddress(passwordEncoder.encode(updateProfileDTO.getAddress()));
        }
        if (updateProfileDTO.getPhone() != null) {
            Member.setPhone(passwordEncoder.encode(updateProfileDTO.getPhone()));
        }
        memberRepository.save(Member);
    }

    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO requestDto, HttpServletResponse res) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        log.info(email);

        try {
            // 이메일로 회원 조회
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Not registered - Please try again"));

            // 비밀번호 검증
            if (!passwordEncoder.matches(password, member.getPassword())) {
                throw new IllegalArgumentException("Invalid password.");
            }

            // Access Token 및 Refresh Token 생성
            String accessToken = jwtUtil.createAccessToken(member.getEmail(), member.getRole());
            String refreshToken = jwtUtil.createRefreshToken();

            // Refresh Token 저장 (Redis에 저장되어야 함)
            tokenService.storeRefreshToken(member.getEmail(), refreshToken);


            // Refresh Token을 응답 헤더에 추가
            res.addHeader("RefreshToken", refreshToken);
            res.addHeader("AccessToken", accessToken);


            // 로그인 성공 응답에 발급받은 토큰들 추가
            LoginResponseDTO responseDTO = new LoginResponseDTO(accessToken, refreshToken, member.getEmail());
            responseDTO.setAccessToken(accessToken);
            responseDTO.setRefreshToken(refreshToken);
            responseDTO.setMessage("Login successful! Welcome, " + member.getEmail() + "!");

            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            log.error("Error during login: {}", e.getMessage());
            LoginResponseDTO responseDTO = new LoginResponseDTO(null, null, e.getMessage());
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
    }

    public void logout(String accessToken) {
        String token = accessToken.replace(JwtUtil.BEARER_PREFIX, "");
        if (jwtUtil.validateTokenConsideringBlacklist(token)) {
            tokenService.addToBlacklist(accessToken);
        } else {
            throw new IllegalArgumentException("Invalid or expired JWT token.");
        }
    }
}
