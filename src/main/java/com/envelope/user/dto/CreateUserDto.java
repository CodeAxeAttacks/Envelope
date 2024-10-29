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
public class CreateUserDto {

    @Email(message = "Field \"email\" must have an email format")
    private final String email;

    @NotBlank(message = "Field \"password\" must not be blank")
    @Size(max = 64, message = "Field's \"password\" length must be lower than 65 symbols")
    private final String password;

    @NotBlank(message = "Field \"firstname\" must not be blank")
    @Size(max = 64, message = "Field's \"firstname\" length must be lower than 65 symbols")
    private final String firstName;

    @NotBlank(message = "Field \"lastname\" must not be blank")
    @Size(max = 64, message = "Field's \"lastname\" length must be lower than 65 symbols")
    private final String lastName;

    @NotBlank(message = "Field \"phone\" must not be blank")
    private final String phone;

}
