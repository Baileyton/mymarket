package com.user.controller;

import com.core.global.util.AuthenticationUtils;
import com.core.user.dto.UpdatePasswordDto;
import com.core.user.dto.UpdateUserDto;
import com.core.user.dto.UserDto;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/update")
    public UserDto updateUser(@RequestBody UpdateUserDto userDto) {
        Long userId = AuthenticationUtils.getUserIdByToken();
        return userService.updateUser(userId, userDto);
    }

    @PostMapping("/update-password")
    public void updatePassword(@RequestBody UpdatePasswordDto passwordDto) {
        Long userId = AuthenticationUtils.getUserIdByToken();
        userService.updatePassword(userId, passwordDto);
    }

    @DeleteMapping("/delete")
    public void deleteUser() {
        Long userId = AuthenticationUtils.getUserIdByToken();
        userService.deleteUser(userId);
    }
}
