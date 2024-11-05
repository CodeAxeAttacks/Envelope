package com.envelope.user.controller;

import com.envelope.user.dto.AuthUserDto;
import com.envelope.user.dto.LoginUserDto;
import com.envelope.user.dto.PatchUserDto;
import com.envelope.user.dto.RegisterUserDto;
import com.envelope.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    
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
