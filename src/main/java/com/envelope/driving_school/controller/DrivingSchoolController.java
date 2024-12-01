package com.envelope.driving_school.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.envelope.driving_school.dto.additional_service.AddAdditionalServiceDto;
import com.envelope.driving_school.dto.additional_service.AdditionalServiceDto;
import com.envelope.driving_school.dto.driving_school.DrivingSchoolDto;
import com.envelope.driving_school.dto.driving_school.PatchDrivingSchoolDto;
import com.envelope.driving_school.dto.driving_school.RegisterDrivingSchoolDto;
import com.envelope.driving_school.dto.promotion.AddPromotionDto;
import com.envelope.driving_school.dto.promotion.PromotionDto;
import com.envelope.driving_school.service.DrivingSchoolService;
import com.envelope.instructor.dto.instructor.InstructorDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DrivingSchoolController {

    private final DrivingSchoolService service;

    @GetMapping("/driving-school")
    public List<DrivingSchoolDto> getAll() {
        log.info("Get all driving schools");
        return service.getAll();
    }

    @GetMapping("/driving-school/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable("id") Long id) {
        log.info("Get image for driving school with id: {}", id);
        return service.getImage(id);
    }

    @GetMapping("/driving-school/{id}")
    public DrivingSchoolDto getById(@PathVariable("id") Long id) {
        log.info("Getting school with id: {}", id);
        return service.getById(id);
    }

    @GetMapping("/driving-school/{id}/service")
    public List<AdditionalServiceDto> getAllServicesById(@PathVariable("id") Long id) {
        log.info("Getting all services from school with id: {}", id);
        return service.getAllServicesById(id);
    }

    @GetMapping("/user/{id}/driving-school")
    public List<DrivingSchoolDto> getAllByUserId(@PathVariable("id") Long id) {
        log.info("Getting all driving schools for user with id: {}", id);
        return service.getAllByUserId(id);
    }

    @GetMapping("/driving-school/{id}/instructor")
    public List<InstructorDto> getAllInstructorsById(@PathVariable("id") Long id) {
        log.info("Get all instrutors for driving school with id: {}", id);
        return service.getAllInstructorsById(id);
    }

    @PostMapping("/driving-school")
    public DrivingSchoolDto register(@RequestBody @Valid RegisterDrivingSchoolDto registerDrivingSchoolDto) {
        log.info("Register driving school: {}", registerDrivingSchoolDto);
        return service.register(registerDrivingSchoolDto);
    }

    @PostMapping("/driving-school/{id}/additional-service")
    public AdditionalServiceDto addAdditionalService(
            @RequestBody @Valid AddAdditionalServiceDto addAdditionalServiceDto,
            @PathVariable(name = "id") Long drivingSchoolId) {
        log.info("Add additional service to driving school with id {}: {}", drivingSchoolId, addAdditionalServiceDto);
        return service.addAdditionalService(addAdditionalServiceDto, drivingSchoolId);
    }

    @PostMapping("/driving-school/{id}/promotion")
    public PromotionDto addPromotion(
            @RequestBody @Valid AddPromotionDto addPromotionDto,
            @PathVariable(name = "id") Long drivingSchoolId) {
        log.info("Add promotion to driving school with id {}: {}", drivingSchoolId, addPromotionDto);
        return service.addPromotion(addPromotionDto, drivingSchoolId);
    }

    @PostMapping("/driving-school/{id}/instructor/{email}")
    public InstructorDto addInstructorByEmail(@PathVariable("id") Long id, @PathVariable("email") @Valid @Email String email) {
        log.info("Add instructor with email {} to driving school with id {}", email, id);
        return service.addInstructorByEmail(id, email);
    }

    @PatchMapping("/driving-school/{id}")
    public DrivingSchoolDto patch(@PathVariable("id") Long id, @RequestBody @Valid PatchDrivingSchoolDto patchDrivingSchoolDto) {
        log.info("Patching driving school with id {} by {}", id, patchDrivingSchoolDto);
        return service.patchById(id, patchDrivingSchoolDto);
    }

    @PatchMapping("/driving-school/{id}/image")
    public void setImage(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) {
        log.info("Set image for driving schoool with id: {}", id);
        service.setImage(file, id);
    }

    @DeleteMapping("/driving-school/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        log.info("Deleting driving school with id: {}", id);
        service.deleteById(id);
    }

    @DeleteMapping("/driving-school/{drivingSchoolId}/additional-service/{additionalServiceId}")
    public void deleteAdditionalServiceById(@PathVariable("drivingSchoolId") Long drivingSchoolId, @PathVariable("additionalServiceId") Long additionalServiceId) {
        log.info("Deleting additional service with id {} from driving school with id {}", additionalServiceId, drivingSchoolId);
        service.deleteAdditionalServiceById(drivingSchoolId, additionalServiceId);
    }

    @DeleteMapping("/driving-school/{drivingSchoolId}/instructor/{instructorId}")
    public void deleteInstructorById(@PathVariable("drivingSchoolId") Long drivingSchoolId, @PathVariable("instructorId") Long instructorId) {
        log.info("Delete instructor with id {} from driving school with id {}", instructorId, drivingSchoolId);
        service.deleteInstructorById(drivingSchoolId, instructorId);
    }
}
