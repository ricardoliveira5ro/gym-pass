package com.ricardo.GymPass.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "T_GYM")
@Getter
@Setter
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ADDRESS", nullable = false, length = 500)
    private String address;

    @Column(name = "CITY", nullable = false, length = 100)
    private String city;

    @Column(name = "STATE", nullable = false, length = 50)
    private String state;

    @Column(name = "POSTAL_CODE", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "COUNTRY", nullable = false, length = 100)
    private String country = "Portugal";

    @Column(name = "PHONE", length = 50)
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TIMEZONE", nullable = false, length = 50)
    private String timezone = "Europe/Lisbon";

    @Column(name = "OPENING_TIME")
    private LocalTime openingTime;

    @Column(name = "CLOSING_TIME")
    private LocalTime closingTime;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean active = true;

    @Column(name = "MAX_CAPACITY")
    private Integer maxCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private User owner;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    public Gym() {
    }
}
