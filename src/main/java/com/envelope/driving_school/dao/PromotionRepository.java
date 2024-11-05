package com.envelope.driving_school.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.envelope.driving_school.model.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    
}
