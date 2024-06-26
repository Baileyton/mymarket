package com.mymarket.service;

import com.mymarket.dto.LoginRequestDto;
import com.mymarket.dto.PasswordUpdateRequestDto;
import com.mymarket.dto.SignupRequestDto;
import com.mymarket.dto.UpdateProfileRequestDto;
import com.mymarket.entity.User;
import com.mymarket.entity.UserRoleEnum;
import com.mymarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signUp(SignupRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입 되어 있는 이메일 입니다.");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        String encodedPhone = passwordEncoder.encode(requestDto.getPhone());
        String encodedAddress = passwordEncoder.encode(requestDto.getAddress());

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .phone(encodedPhone)
                .address(encodedAddress)
                .created_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modified_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role(UserRoleEnum.USER) // 기본 역할 설정
                .build();

        return userRepository.save(user);
    }

    public void updatePassword(User user, PasswordUpdateRequestDto requestDto) {
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        String newEncodedPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(newEncodedPassword);
        userRepository.save(user);
    }

    public void updateProfile(User user, UpdateProfileRequestDto requestDto) {
        user.updateAddress(passwordEncoder.encode(requestDto.getNewAddress()));
        user.updatePhone(passwordEncoder.encode(requestDto.getNewPhone()));
        userRepository.save(user);
    }
}
