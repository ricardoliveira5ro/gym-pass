package com.ricardo.GymPass.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDate;

public record ExternalMembershipDto(
    @JsonProperty("id") String id,
    @JsonProperty("member_id") String memberId,
    @JsonProperty("status") String status,
    @JsonProperty("tier") String tier,
    @JsonProperty("start_date") LocalDate startDate,
    @JsonProperty("end_date") LocalDate endDate,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt
) {}