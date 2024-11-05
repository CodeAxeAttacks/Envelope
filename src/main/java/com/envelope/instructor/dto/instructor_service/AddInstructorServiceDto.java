package com.envelope.instructor.dto.instructor_service;

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
public class AddInstructorServiceDto {

    @NotBlank(message = "Field \"name\" must not be blank")
    @Size(max = 64, message = "Field's \"name\" length must be lower than 65 symbols")
    private String name;

    @NotBlank(message = "Field \"name\" must not be blank")
    @Size(max = 64, message = "Field's \"name\" length must be lower than 65 symbols")
    private String description;

    @NotNull(message = "Field \"price\" must not be null")
    @PositiveOrZero(message = "Field \"price\" must not be negative number")
    private Float price;

}
