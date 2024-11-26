package com.envelope.user.service;

import com.envelope.exception.exceptions.InvalidInputException;
import com.envelope.exception.exceptions.ObjectAlreadyExistsException;
import com.envelope.exception.exceptions.ObjectNotFoundException;
import com.envelope.exception.exceptions.UserNotAuthenticatedException;
import com.envelope.instructor.dao.InstructorRepository;
import com.envelope.instructor.model.Instructor;
import com.envelope.security.JwtService;
import com.envelope.user.dao.UserRepository;
import com.envelope.user.dto.AuthUserDto;
import com.envelope.user.dto.LoginUserDto;
import com.envelope.user.dto.PatchUserDto;
import com.envelope.user.dto.RegisterUserDto;
import com.envelope.user.dto.UserDto;
import com.envelope.user.model.Role;
import com.envelope.user.model.Status;
import com.envelope.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserDto whoAmI() {
        User currentUser = jwtService.currentUser();
        Instructor instructor = null;

        if (currentUser.getRole() == Role.INSTRUCTOR || currentUser.getRole() == Role.INSTRUCTOR_AND_ADMINISTRATOR)
            instructor = instructorRepository.findByUser(currentUser).get();

        return UserDto.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .phone(currentUser.getPhone())
                .createdAt(currentUser.getCreatedAt())
                .role(currentUser.getRole())
                .status(currentUser.getStatus())
                .instructorId(instructor != null ? instructor.getId() : null)
                .build();
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .createdAt(user.getCreatedAt())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .build())
                .toList();
    }

    public UserDto getById(Long userId) {
        if (userId == null || userId < 0) {
            String errorMessage = "User's id must not be null or negative";
            log.warn(errorMessage);
            throw new InvalidInputException(errorMessage);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            String errorMessage = String.format("User with id %d does not exist", userId);
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
        }
        User user = userOptional.get();
        Instructor instructor = null;

        if (user.getRole() == Role.INSTRUCTOR || user.getRole() == Role.INSTRUCTOR_AND_ADMINISTRATOR)
            instructor = instructorRepository.findByUser(user).get();

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .role(user.getRole())
                .status(user.getStatus())
                .instructorId(instructor != null ? instructor.getId() : null)
                .build();
    }

    public AuthUserDto login(LoginUserDto loginUserDto) {
        Optional<User> userOptional = userRepository.findByEmail(loginUserDto.getEmail());
        if (userOptional.isEmpty()) {
            String errorMessage = String.format("User with email %s does not exist", loginUserDto.getEmail());
            log.warn(errorMessage);
            throw new ObjectNotFoundException(errorMessage);
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

    @Transactional(readOnly = false)
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

    @Transactional(readOnly = false)
    public AuthUserDto patch(PatchUserDto patchUserDto) {
        User user = jwtService.currentUser();

        if (patchUserDto.getFirstName() != null)
            user.setFirstName(patchUserDto.getFirstName());
        if (patchUserDto.getLastName() != null)
            user.setLastName(patchUserDto.getLastName());
        if (patchUserDto.getPhone() != null)
            user.setPhone(patchUserDto.getPhone());

        user = userRepository.save(user);
        log.info("User patched: {}", user);
        String jwt = jwtService.generateToken(user);

        return AuthUserDto.builder()
                .token(jwt)
                .build();
    }

}
