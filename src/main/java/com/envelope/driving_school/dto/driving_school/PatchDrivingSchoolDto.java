package com.envelope.driving_school.dto.driving_school;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class PatchDrivingSchoolDto {
    @Size(max = 64, message = "Field's \"name\" length must be lower than 65 symbols")
    private final String name;

    @Size(max = 128, message = "Field's \"address\" length must be lower than 129 symbols")
    private final String address;

    @Size(max = 12, message = "Field's \"phoneNumber\" length must be lower than 13 symbols")
    private final String phoneNumber;

    @Size(max = 512, message = "Field's \"description\" length must be lower than 513 symbols")
    private final String description;
}
