package com.envelope.instructor.dto.instructor;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class PatchInstructorDto {

    @Size(max = 512, message = "Field's \"description\" length must be lower than 513 symbols")
    private final String description;

    private final Integer experience;

    private final Float rating;

}
