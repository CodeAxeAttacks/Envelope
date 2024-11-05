package com.envelope.driving_school.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envelope.driving_school.service.DrivingSchoolService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/driving-school")
@RequiredArgsConstructor
public class DrivingSchoolController {
    
    private final DrivingSchoolService service;

}
