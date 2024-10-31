package com.envelope.course.service;

import com.envelope.course.dao.CourseRepository;
import com.envelope.course.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Course updateCourse(Long id, Course updatedCourse) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setName(updatedCourse.getName());
                    course.setPrice(updatedCourse.getPrice());
                    course.setDuration(updatedCourse.getDuration());
                    course.setDescription(updatedCourse.getDescription());
                    course.setVehicleCategory(updatedCourse.getVehicleCategory());
                    course.setStudyFormat(updatedCourse.getStudyFormat());
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

}
