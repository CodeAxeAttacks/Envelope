package com.envelope.vehicle.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model", nullable = false, length = 64)
    private String model;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "transmission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;

}
