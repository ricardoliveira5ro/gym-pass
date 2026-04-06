package com.ricardo.GymPass.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record ExternalMemberDto(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("email") String email,
    @JsonProperty("phone") String phone,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt
) {}