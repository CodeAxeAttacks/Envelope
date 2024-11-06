package com.envelope.user.controller;

import com.envelope.user.dto.AuthUserDto;
import com.envelope.user.dto.LoginUserDto;
import com.envelope.user.dto.PatchUserDto;
import com.envelope.user.dto.RegisterUserDto;
import com.envelope.user.dto.UserDto;
import com.envelope.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Getting all users");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable(name = "id") Long userId) {
        log.info("Getting user with id: {}", userId);
        return userService.getById(userId);
    }
    
    @PostMapping("/login")
    public AuthUserDto postMethodName(@RequestBody @Valid LoginUserDto loginUserDto) {
        log.info("Login user: {}", loginUserDto);
        return userService.login(loginUserDto);
    }
    
    @PostMapping("/register")
    public AuthUserDto register(@RequestBody @Valid RegisterUserDto createUserDto) {
        log.info("Register user: {}", createUserDto);
        return userService.register(createUserDto);
    }

    @PatchMapping
    public AuthUserDto patch(@RequestBody @Valid PatchUserDto patchUserDto) {
        log.info("Patch user: {}", patchUserDto);
        return userService.patch(patchUserDto);
    }

}
