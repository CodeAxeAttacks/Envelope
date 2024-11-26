package com.envelope.course.controller;

import com.envelope.course.dto.RegisterCourseDto;
import com.envelope.course.dto.CourseDto;
import com.envelope.course.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService service;

    @GetMapping("/course")
    public List<CourseDto> getAll(@PathVariable(name = "id") Long drivingSchoolId) {
        log.info("Get courses from driving school with id: {}", drivingSchoolId);
        return service.getAll();
    }

    @PostMapping("/driving-school/{id}/course")
    public CourseDto register(@RequestBody @Valid RegisterCourseDto registerCourseDto, @PathVariable(name = "id") Long drivingSchoolId) {
        log.info("Register course to driving school with id {}: {}", drivingSchoolId, registerCourseDto);
        return service.register(registerCourseDto, drivingSchoolId);
    }

    @DeleteMapping("/driving-school/{drivingSchoolId}/course/{courseId}")
    public void deleteById(@PathVariable("drivingSchoolId") Long drivingSchoolId, @PathVariable("courseId") Long courseId) {
        log.info("Deleting course with id {} from driving school with id {}", courseId, drivingSchoolId);
        service.deleteById(drivingSchoolId, courseId);
    }

}
