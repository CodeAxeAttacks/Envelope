package com.envelope.course.controller;

import com.envelope.course.dto.RegisterCourseDto;
import com.envelope.course.dto.CourseDto;
import com.envelope.course.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService service;

    public CourseDto register(@RequestBody @Valid RegisterCourseDto registerCourseDto) {
        log.info("Register course: {}", registerCourseDto);
        return service.register(registerCourseDto);
    }

}
