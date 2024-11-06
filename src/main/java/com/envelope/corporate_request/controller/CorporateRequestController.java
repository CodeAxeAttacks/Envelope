package com.envelope.corporate_request.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envelope.corporate_request.dto.CorporateRequestDto;
import com.envelope.corporate_request.dto.RegisterCorporateRequestDto;
import com.envelope.corporate_request.service.CorporateRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/corporate-request")
@RequiredArgsConstructor
@Slf4j
public class CorporateRequestController {
    
    private final CorporateRequestService service;

    @PostMapping
    public CorporateRequestDto register(@RequestBody @Valid RegisterCorporateRequestDto registerCorporateRequestDto) {
        log.info("Register corporate request: {}", registerCorporateRequestDto);
        return service.register(registerCorporateRequestDto);
    }

}
