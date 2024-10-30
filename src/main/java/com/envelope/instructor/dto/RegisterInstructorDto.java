package com.envelope.instructor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class RegisterInstructorDto {
    
    @NotNull(message = "Field \"userId\" must not be null")
    @Positive(message = "Field \"userId\" must be a positive integer")
    private final Long userId;

    @NotNull(message = "Field \"experience\" must not be null")
    @Positive(message = "Field \"experience\" must be a positive integer")
    private final Integer experience;

    @NotBlank(message = "Field \"description\" must not be blank")
    @Size(max = 512, message = "Field's \"description\" length must be lower than 513 symbols")
    private final String description;

}
