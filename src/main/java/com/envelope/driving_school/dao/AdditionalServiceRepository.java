package com.envelope.driving_school.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.envelope.driving_school.model.AdditionalService;

public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Long> {
    
}
