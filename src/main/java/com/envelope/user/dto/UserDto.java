package com.envelope.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;

import com.envelope.user.model.Role;
import com.envelope.user.model.Status;

@Getter
@Builder(toBuilder = true)
public class UserDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Instant createdAt;

    private Role role;

    private Status status;

    private Long instructorId;

}
