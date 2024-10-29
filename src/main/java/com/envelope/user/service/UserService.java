package com.envelope.user.service;

import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import com.envelope.user.dao.UserRepository;
import com.envelope.user.dto.CreateUserDto;
import com.envelope.user.dto.ResultUserDto;
import com.envelope.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public ResultUserDto register(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isEmpty()) {
            String errorMessage = String.format("User with email %s already exists", createUserDto.getEmail());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }

        User user = User.builder()
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .phone(createUserDto.getPhone())
                .build();
        user = userRepository.save(user);
        log.info("User registered: {}", user);
        return ResultUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
//                .role(user.getRole())
//                .status(user.getStatus())
                .build();
    }

}
