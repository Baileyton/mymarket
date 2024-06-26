package com.mymarket;

import com.mymarket.contoller.UserController;
import com.mymarket.dto.SignupRequestDto;
import com.mymarket.entity.User;
import com.mymarket.entity.UserRoleEnum;
import com.mymarket.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUp_Success() {
        // 가상의 회원가입 요청 DTO 생성
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setName("Test User");
        requestDto.setEmail("testuser@example.com");
        requestDto.setPassword("password123");
        requestDto.setPhone("010-1234-5678");
        requestDto.setAddress("Seoul, Korea");

        // 가상의 회원 정보 생성
        User newUser = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password("encodedPassword") // 실제 비밀번호 인코딩 결과
                .phone("encodedPhone") // 실제 전화번호 인코딩 결과
                .address("encodedAddress") // 실제 주소 인코딩 결과
                .created_at(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role(UserRoleEnum.USER)
                .build();

        // UserService의 signUp 메서드가 호출될 때 반환할 값을 설정
        when(userService.signUp(requestDto)).thenReturn(newUser);

        // UserController의 signUp 메서드 호출 및 반환 값 확인
        ResponseEntity<?> responseEntity = userController.signUp(requestDto);

        // 반환된 ResponseEntity 검증
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // 반환된 User 객체 검증
        User returnedUser = (User) responseEntity.getBody();
        assertNotNull(returnedUser);
        assertEquals(requestDto.getName(), returnedUser.getName());
        assertEquals(requestDto.getEmail(), returnedUser.getEmail());
        // 추가적인 필드 검증 가능

        // UserService의 signUp 메서드가 1번 호출되었는지 검증
        verify(userService, times(1)).signUp(requestDto);
    }

    @Test
    public void testSignUp_EmailAlreadyExists() {
        // 가상의 이미 가입된 이메일을 가진 회원가입 요청 DTO 생성
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setName("Test User");
        requestDto.setEmail("existinguser@example.com"); // 이미 존재하는 이메일
        requestDto.setPassword("password123");
        requestDto.setPhone("010-1234-5678");
        requestDto.setAddress("Seoul, Korea");

        // 이미 존재하는 이메일을 가진 사용자가 존재한다고 설정
        when(userService.signUp(requestDto)).thenThrow(new IllegalArgumentException("이미 가입 되어 있는 이메일 입니다."));

        // UserController의 signUp 메서드 호출 및 예외 처리 확인
        ResponseEntity<?> responseEntity = userController.signUp(requestDto);

        // 반환된 ResponseEntity 검증
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("이미 가입 되어 있는 이메일 입니다.", responseEntity.getBody());

        // UserService의 signUp 메서드가 1번 호출되었는지 검증
        verify(userService, times(1)).signUp(requestDto);
    }
}