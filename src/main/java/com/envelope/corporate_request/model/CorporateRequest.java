package com.envelope.corporate_request.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.envelope.course.model.VehicleCategory;
import com.envelope.user.model.User;
import com.envelope.vehicle.model.Transmission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "corporate_requests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class CorporateRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false, length = 64)
    private String companyName;

    @Column(name = "employee_count", nullable = false)
    private Integer employeeCount;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Column(name = "vehicle_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleCategory vehicleCategory;

    @Column(name = "transmission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
}
