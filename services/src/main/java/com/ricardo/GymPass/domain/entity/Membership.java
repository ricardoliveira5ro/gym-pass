package com.ricardo.GymPass.domain.entity;

import com.ricardo.GymPass.domain.enums.MembershipStatus;
import com.ricardo.GymPass.domain.enums.MembershipTier;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "T_MEMBERSHIPS")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "EXTERNAL_ID")
    private String externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GYM_ID", nullable = false)
    private Gym gym;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 50)
    private MembershipStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIER", nullable = false, length = 50)
    private MembershipTier tier;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    public Membership() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public MembershipTier getTier() {
        return tier;
    }

    public void setTier(MembershipTier tier) {
        this.tier = tier;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
