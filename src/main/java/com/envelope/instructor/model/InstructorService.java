package com.envelope.instructor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "instructor_services")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class InstructorService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Column(name = "price", nullable = false)
    private Float price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

}
