package com.ricardo.GymPass.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public record SyncResponse(
    @JsonProperty("date") LocalDate date,
    @JsonProperty("members") List<ExternalMemberDto> members,
    @JsonProperty("memberships") List<ExternalMembershipDto> memberships
) {}