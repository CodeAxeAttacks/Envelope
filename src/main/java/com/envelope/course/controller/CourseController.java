package com.envelope.course.controller;

import com.envelope.course.dto.RegisterCourseDto;
import com.envelope.course.dto.CourseDto;
import com.envelope.course.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driving-school")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService service;

    @PostMapping("/{id}/course")
    public CourseDto register(@RequestBody @Valid RegisterCourseDto registerCourseDto, @PathVariable Long drivingSchoolId) {
        log.info("Register course to driving school with id {}: {}", drivingSchoolId, registerCourseDto);
        return service.register(registerCourseDto, drivingSchoolId);
    }

}
