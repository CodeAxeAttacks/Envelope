package com.envelope.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class RegisterReviewDto {
    
    @NotNull(message = "Field \"rate\" must not be null")
    @Min(value = 0, message = "Field \"rate\" must not be lower than 0")
    @Max(value = 5, message = "Field \"rate\" must not be higher than 5")
    private final Float rate;

    @NotBlank(message = "Field \"review\" must not be blank")
    @Size(max = 512, message = "Field's \"review\" length must be lower than 513 symbols")
    private final String review;

}
