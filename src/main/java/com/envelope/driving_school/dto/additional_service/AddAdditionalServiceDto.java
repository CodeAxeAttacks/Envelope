package com.envelope.driving_school.dto.additional_service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class AddAdditionalServiceDto {
    
    @NotBlank(message = "Field \"name\" must not be blank")
    @Size(max = 64, message = "Field's \"name\" length must be lower than 65 symbols")
    private final String name;

    @NotBlank(message = "Field \"description\" must not be blank")
    @Size(max = 512, message = "Field's \"description\" length must be lower that 513 symbols")
    private final String description;

    @NotNull(message = "Field \"price\" must not be null")
    @PositiveOrZero(message = "Field \"price\" must not be negative number")
    private final Float price;

}
