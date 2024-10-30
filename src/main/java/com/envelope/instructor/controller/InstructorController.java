package com.envelope.instructor.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envelope.instructor.dto.RegisterInstructorDto;
import com.envelope.instructor.dto.ResultInstructorDto;
import com.envelope.instructor.service.InstructorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
@Slf4j
public class InstructorController {

    private final InstructorService service;

    @PostMapping
    public ResultInstructorDto register(@RequestBody @Valid RegisterInstructorDto registerInstructorDto) {
        log.info("Register instructor: {}", registerInstructorDto);
        return service.register(registerInstructorDto);
    }
    
}
