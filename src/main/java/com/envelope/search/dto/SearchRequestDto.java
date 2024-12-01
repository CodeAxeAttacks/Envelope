package com.envelope.search.dto;

import java.util.List;

import com.envelope.course.model.StudyFormat;
import com.envelope.vehicle.model.Category;
import com.envelope.vehicle.model.Transmission;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class SearchRequestDto {

    @PositiveOrZero(message = "Field \"minPrice\" must not be negative")
    private final float minPrice;

    @PositiveOrZero(message = "Field \"maxPrice\" must not be negative")
    private final float maxPrice;

    @NotNull(message = "Field \"categories\" must not be null")
    private final List<Category> categories;

    @PositiveOrZero(message = "Field \"minRating\" must not be negative")
    @Max(value=5, message="Field \"minRating\" must not be greater than 5")
    private float minRating;

    private Float maxDuration;

    @NotNull(message = "Field \"transmissions\" must not be null")
    private List<Transmission> transmissions;    

    private List<StudyFormat> studyFormats;
}
