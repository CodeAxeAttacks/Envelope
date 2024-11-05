package com.envelope.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class AuthUserDto {
    private String token;
}
