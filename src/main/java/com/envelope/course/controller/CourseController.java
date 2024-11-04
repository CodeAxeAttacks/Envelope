package com.envelope.course.controller;

import com.envelope.course.model.Course;
import com.envelope.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.envelope.course.dto.CreateCourseDto;
import com.envelope.course.dto.CourseDto;
import com.envelope.exception.exceptions.ObjectNotFoundException;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody @Valid CreateCourseDto createCourseDto) {
        Course course = courseService.createCourse(createCourseDto);
        return ResponseEntity.ok(courseService.convertToDto(course));
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        // Преобразование списка в DTO
        List<CourseDto> courseDtos = courses.stream()
                .map(courseService::convertToDto)
                .toList();
        return ResponseEntity.ok(courseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(courseService.convertToDto(course));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        try {
            Course course = courseService.updateCourse(id, updatedCourse);
            return ResponseEntity.ok(courseService.convertToDto(course));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
