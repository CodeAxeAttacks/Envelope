package com.envelope.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.envelope.course.dao.CourseRepository;
import com.envelope.driving_school.dao.AdditionalServiceRepository;
import com.envelope.instructor.dao.InstructorServiceRepository;
import com.envelope.instructor.model.InstructorService;
import com.envelope.search.dto.SearchRequestDto;
import com.envelope.search.dto.SearchResultDto;
import com.envelope.vehicle.dao.VehicleRepository;
import com.envelope.vehicle.model.Category;
import com.envelope.vehicle.model.Transmission;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CourseRepository courseRepository;
    private final InstructorServiceRepository instructorServiceRepository;
    private final VehicleRepository vehicleRepository;
    private final AdditionalServiceRepository additionalServiceRepository;

    public List<SearchResultDto> searchInstructors(SearchRequestDto searchRequestDto) {
        List<InstructorService> services = instructorServiceRepository.findAll().stream()
            .filter(service -> service.getPrice() >= searchRequestDto.getMinPrice() && service.getPrice() <= searchRequestDto.getMaxPrice())
            .filter(service -> service.getInstructor().getRating() >= searchRequestDto.getMinRating())
            .filter(service -> {
                List<Category> categories = vehicleRepository.findAllCategoriesByInstructorId(service.getInstructor().getId());
                boolean isMatch = false;
                for (Category category : categories) {
                    if (searchRequestDto.getCategories().contains(category)) {
                        isMatch = true;
                        continue;
                    }
                }
                return isMatch;
            }).filter(service -> {
                List<Transmission> transmissions = vehicleRepository.findAllTransmissionsByInstructorId(service.getInstructor().getId());
                boolean isMatch = false;
                for (Transmission transmission : transmissions) {
                    if (searchRequestDto.getTransmissions().contains(transmission)) {
                        isMatch = true;
                        break;
                    }
                }
                return isMatch;
            })
            .toList();

        Map<Long, Float> avgPrices = new HashMap<>();
        Map<Long, Integer> counts = new HashMap<>();
        Map<Long, Float> rates = new HashMap<>();
        Map<Long, String> names = new HashMap<>();
        for (InstructorService service : services) {
            Long instructorId = service.getInstructor().getId();
            if (avgPrices.containsKey(instructorId)) {
                Float prevValue = avgPrices.get(instructorId);
                Integer prevCount = counts.get(instructorId);
                avgPrices.put(instructorId, (prevValue + service.getPrice()) / (prevCount + 1));
                counts.put(instructorId, prevCount + 1);
            } else {
                avgPrices.put(instructorId, service.getPrice());
                counts.put(instructorId, 1);
                rates.put(instructorId, service.getInstructor().getRating());
                names.put(instructorId, service.getInstructor().getUser().getLastName() + service.getInstructor().getUser().getFirstName());
            }
        }

        List<SearchResultDto> results = new ArrayList<>();
        for (Long instructorId : avgPrices.keySet()) {
            results.add(SearchResultDto.builder()
                .id(instructorId)
                .name(names.get(instructorId))
                .avgPrice(avgPrices.get(instructorId))
                .rate(rates.get(instructorId))
                .build());
        }

        return results;
    }
}