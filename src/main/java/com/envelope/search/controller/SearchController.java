package com.envelope.search.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envelope.search.dto.SearchRequestDto;
import com.envelope.search.dto.SearchResultDto;
import com.envelope.search.service.SearchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService service;

    @PostMapping("/driving-school")
    public List<SearchResultDto> searchDrivingSchools(@RequestBody @Valid SearchRequestDto searchRequestDto) {
        log.info("Getting all instructors by filters: {}", searchRequestDto);
        return service.searchDrivingSchools(searchRequestDto);
    }
    
    @PostMapping("/instructor")
    public List<SearchResultDto> searchInstructors(@RequestBody @Valid SearchRequestDto searchRequestDto) {
        return service.searchInstructors(searchRequestDto);
    }

}
