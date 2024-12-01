package com.envelope.course.model;

import com.envelope.driving_school.model.DrivingSchool;
import com.envelope.vehicle.model.Category;
import com.envelope.vehicle.model.Transmission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "duration", nullable = false)
    private Float duration;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "transmission", nullable = false)
    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @Column(name = "study_format", nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyFormat studyFormat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driving_school_id", nullable = false)
    private DrivingSchool drivingSchool;

}
