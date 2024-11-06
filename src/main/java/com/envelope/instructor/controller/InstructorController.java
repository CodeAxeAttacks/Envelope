package com.envelope.instructor.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envelope.instructor.dto.instructor.RegisterInstructorDto;
import com.envelope.instructor.dto.instructor_service.AddInstructorServiceDto;
import com.envelope.instructor.dto.instructor_service.InstructorServiceDto;
import com.envelope.instructor.dto.instructor_service.PatchInstructorServiceDto;
import com.envelope.instructor.dto.instructor.InstructorDto;
import com.envelope.instructor.dto.instructor.PatchInstructorDto;
import com.envelope.instructor.service.InstructorService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
@Slf4j
public class InstructorController {

    private final InstructorService service;

    @GetMapping
    public List<InstructorDto> getAll() {
        log.info("Get all instructors");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public InstructorDto getById(@PathVariable(name = "id") Long instructorId) {
        log.info("Get an instructor with id {}", instructorId);
        return service.getById(instructorId);
    }

    @GetMapping("/{id}/service")
    public List<InstructorServiceDto> getAllInstructorServicesByInstructorId(@PathVariable(name = "id") Long instructorId) {
        log.info("Get al; instructor's with id {} services", instructorId);
        return service.getAllInstructorServicesByInstructorId(instructorId);
    }

    @PostMapping
    public InstructorDto register(@RequestBody @Valid RegisterInstructorDto registerInstructorDto) {
        log.info("Register instructor: {}", registerInstructorDto);
        return service.register(registerInstructorDto);
    }

    @PostMapping("/service")
    public InstructorServiceDto addService(@RequestBody @Valid AddInstructorServiceDto addInstructorServiceDto) {
        log.info("Add service: {}", addInstructorServiceDto);
        return service.addService(addInstructorServiceDto);
    }
    
    @DeleteMapping("/service/{id}")
    public void deleteService(@PathVariable(name = "id") @Positive long serviceId) {
        log.info("Delete service with id: {}", serviceId);
        service.deleteService(serviceId);
    }

    @PatchMapping
    public InstructorDto patchInstructor(@RequestBody @Valid PatchInstructorDto patchInstructorDto) {
        log.info("Patch instructor: {}", patchInstructorDto);
        return service.patchInstructor(patchInstructorDto);
    }

    @PatchMapping("/service/{id}")
    public InstructorServiceDto patchInstructorService(
        @PathVariable(name = "id") @Positive Long serviceId,
        @RequestBody @Valid PatchInstructorServiceDto patchInstructorServiceDto) {
            log.info("Patch instructor service with id: {}", serviceId);
            return service.patchInstructorService(serviceId, patchInstructorServiceDto);
    } 

}
