package com.envelope.user.service;

import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import com.envelope.exception.exceptions.UserNotAuthenticatedException;
import com.envelope.security.JwtService;
import com.envelope.user.dao.UserRepository;
import com.envelope.user.dto.AuthUserDto;
import com.envelope.user.dto.LoginUserDto;
import com.envelope.user.dto.PatchUserDto;
import com.envelope.user.dto.RegisterUserDto;
import com.envelope.user.model.Role;
import com.envelope.user.model.Status;
import com.envelope.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthUserDto login(LoginUserDto loginUserDto) {
        Optional<User> userOptional = userRepository.findByEmail(loginUserDto.getEmail());
        if (userOptional.isEmpty()) {
            String errorMessage = String.format("User with email %s does not exist", loginUserDto.getEmail());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
            String errorMessage = String.format("Invalid password for user with email %s", loginUserDto.getEmail());
            log.warn(errorMessage);
            throw new UserNotAuthenticatedException(errorMessage);
        }

        String jwt = jwtService.generateToken(user);

        return AuthUserDto.builder()
                .token(jwt)
                .build();
    }

    public AuthUserDto register(RegisterUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            String errorMessage = String.format("User with email %s already exists", createUserDto.getEmail());
            log.warn(errorMessage);
            throw new ObjectAlreadyExistsException(errorMessage);
        }

        User user = userRepository.save(User.builder()
                .email(createUserDto.getEmail())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .phone(createUserDto.getPhone())
                .status(Status.LOOKING_FOR_SCHOOL)
                .role(Role.USER)
                .build());
        log.info("User registered: {}", user);
        String jwt = jwtService.generateToken(user);

        return AuthUserDto.builder()
                .token(jwt)
                .build();
    }

    public AuthUserDto patch(PatchUserDto patchUserDto) {
        User user = jwtService.currentUser();

        if (patchUserDto.getEmail() != null) {
            if (userRepository.findByEmail(patchUserDto.getEmail()).isPresent()) {
                String errorMessage = String.format("User with email %s already exists", patchUserDto.getEmail());
                log.warn(errorMessage);
                throw new ObjectAlreadyExistsException(errorMessage);
            }
            user.setEmail(patchUserDto.getEmail());
        }   
        if (patchUserDto.getFirstName() != null) 
            user.setFirstName(patchUserDto.getFirstName());
        if (patchUserDto.getLastName() != null) 
            user.setLastName(patchUserDto.getLastName());
        if (patchUserDto.getPhone() != null) 
            user.setPhone(patchUserDto.getPhone());
        if (user.getStatus() != null) 
            user.setStatus(user.getStatus());
        if (user.getRole() != null) 
            user.setStatus(user.getStatus());
        
        user = userRepository.save(user);
        log.info("User patched: {}", user);
        String jwt = jwtService.generateToken(user);

        return AuthUserDto.builder()
                .token(jwt)
                .build();
    }

}
