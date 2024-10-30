package com.envelope.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder(toBuilder = true)
public class ResultUserDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Instant createdAt;

//    private Role role;
//
//    private Status status;

}
