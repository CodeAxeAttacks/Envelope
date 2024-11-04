package com.envelope.course.dto;

import com.envelope.course.model.StudyFormat;
import com.envelope.course.model.VehicleCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseDto {

    @NotBlank(message = "Name is required")
    @Size(max = 64, message = "Name must be less than 64 characters")
    private String name;

    @NotNull(message = "Price is required")
    private double price;

    @NotNull(message = "Duration is required")
    private double duration;

    @NotBlank(message = "Description is required")
    @Size(max = 512, message = "Description must be less than 512 characters")
    private String description;

    @NotNull(message = "Vehicle Category is required")
    private VehicleCategory vehicleCategory;

    @NotNull(message = "Study Format is required")
    private StudyFormat studyFormat;

}
