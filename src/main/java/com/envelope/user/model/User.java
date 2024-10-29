package com.envelope.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", unique = true, nullable = false, length = 64)
    private String email;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "firstname", nullable = false, length = 64)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 64)
    private String lastName;

    @Column(name = "phone", length = 12)
    private String phone;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

//    @Column(name = "role", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    @Column(name = "status", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Status status;

}
