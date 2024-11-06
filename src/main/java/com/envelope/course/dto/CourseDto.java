package com.envelope.course.dto;

import com.envelope.course.model.StudyFormat;
import com.envelope.course.model.VehicleCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class CourseDto {

    private Long id;
    private String name;
    private Float price;
    private Float duration;
    private String description;
    private VehicleCategory vehicleCategory;
    private StudyFormat studyFormat;

}
