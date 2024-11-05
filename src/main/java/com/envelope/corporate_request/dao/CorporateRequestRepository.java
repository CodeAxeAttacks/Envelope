package com.envelope.corporate_request.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.envelope.corporate_request.model.CorporateRequest;

public interface CorporateRequestRepository extends JpaRepository<CorporateRequest, Long> {
}
