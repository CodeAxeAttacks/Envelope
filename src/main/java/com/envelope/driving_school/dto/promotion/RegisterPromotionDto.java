package com.envelope.driving_school.dto.promotion;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class RegisterPromotionDto {
    
    @NotBlank(message = "Field \"name\" must not be blank")
    @Size(max = 64, message = "Field's \"name\" length must be lower than 65 symbols")
    private final String name;

    @NotBlank(message = "Field \"description\" must not be blank")
    @Size(max = 64, message = "Field's \"description\" length must be lower than 513 symbols")
    private final String description;

    @NotNull(message = "Field \"start\" must not be null")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private final Instant start;

    @NotNull(message = "Field \"end\" must not be null")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private final Instant end;

}
