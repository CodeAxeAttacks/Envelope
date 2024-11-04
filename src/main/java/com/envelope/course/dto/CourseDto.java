package com.envelope.course.dto;

import com.envelope.course.model.StudyFormat;
import com.envelope.course.model.VehicleCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseDto {

    private Long id;
    private String name;
    private double price;
    private double duration;
    private String description;
    private VehicleCategory vehicleCategory;
    private StudyFormat studyFormat;

}
