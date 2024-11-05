package com.envelope.instructor.dto.instructor_service;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class PatchInstructorServiceDto {

    @Size(max = 64, message = "Field's \"name\" length must be lower than 65 symbols")
    private final String name;

    @Size(max = 512, message = "Field's \"description\" length must be lower than 513 symbols")
    private final String description;

    @NotNull(message = "Field \"price\" cannot be null")
    private final Float price;

}