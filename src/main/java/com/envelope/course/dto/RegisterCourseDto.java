package com.envelope.course.dto;

import com.envelope.course.model.StudyFormat;
import com.envelope.course.model.VehicleCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class RegisterCourseDto {

    @NotBlank(message = "Field \"name\" must not be blank")
    @Size(max = 64, message = "Field's \"name\" length must be lower than 65 symbols")
    private final String name;

    @NotNull(message = "Field \"price\" must not be null")
    private final Float price;

    @NotNull(message = "Field \"duration\" must not be null")
    private final Float duration;

    @NotBlank(message = "Field \"description\" must not be blank")
    @Size(max = 512, message = "Field's \"description\" length must be lower than 513 symbols")
    private final String description;

    @NotNull(message = "Field \"vehicleCategory\" must not be null")
    private final VehicleCategory vehicleCategory;

    @NotNull(message = "Field \"studyFormat\" must not be null")
    private final StudyFormat studyFormat;

}
