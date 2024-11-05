package com.envelope.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class LoginUserDto {
    
    @NotBlank(message = "Field \"email\" must not be blank")
    @Email(message = "Field \"email\" must have an email format")
    @Size(max = 64, message = "Field's \"email\" length must be lower than 65 symbols")
    private final String email;

    @NotBlank(message = "Field \"password\" must not be blank")
    @Size(max = 64, message = "Field's \"password\" length must be lower than 65 symbols")
    private final String password;
    
}
