package com.envelope.user.controller;

import com.envelope.user.dto.CreateUserDto;
import com.envelope.user.dto.ResultUserDto;
import com.envelope.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResultUserDto register(@RequestBody @Valid CreateUserDto createUserDto) {
        log.info("Register user: {}", createUserDto);
        return userService.register(createUserDto);
    }

}
