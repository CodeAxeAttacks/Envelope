package com.envelope.driving_school.model;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.envelope.course.model.Course;
import com.envelope.instructor.model.Instructor;
import com.envelope.user.model.User;
import com.envelope.vehicle.model.Vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "driving_schools")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class DrivingSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "address", nullable = false, length = 128)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 12)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Column(name = "rating", nullable = false)
    private Float rating;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "admin_driving_school", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "driving_school_id"))
    private List<User> admins;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "driving_school_id", nullable = false)
    private List<Course> courses;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "driving_school_id", nullable = false)
    private List<AdditionalService> additionalServices;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "driving_school_id", nullable = false)
    private List<Promotion> promotions;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "driving_school_id", nullable = true)
    private List<Vehicle> vehicles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "instructor_driving_school", joinColumns = @JoinColumn(name = "instructor_id"), inverseJoinColumns = @JoinColumn(name = "driving_school_id"))
    private List<Instructor> instructors;

}
