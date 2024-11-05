package com.envelope.driving_school.dto.driving_school;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class RegisterDrivingSchoolDto {
    
    @NotBlank(message = "Field \"name\" must not be blank")
    @Size(max = 64, message = "Field's \"name\" length must be lower than 65 symbols")
    private final String name;

    @NotBlank(message = "Field \"address\" must not be blank")
    @Size(max = 128, message = "Field's \"address\" length must be lower than 129 symbols")
    private final String address;

    @NotBlank(message = "Field \"phoneNumber\" must not be blank")
    @Size(max = 12, message = "Field's \"phoneNumber\" length must be lower than 13 symbols")
    private final String phoneNumber;

    @NotBlank(message = "Field \"email\" must not be blank")
    @Size(max = 64, message = "Field's \"email\" length must be lower than 65 symbols")
    @Email(message = "Field \"email\" must be an email")
    private final String email;

    @NotBlank(message = "Field \"description\" must not be blank")
    @Size(max = 512, message = "Field's \"description\" length must be lower than 513 symbols")
    private final String description;

}
