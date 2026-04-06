package com.ricardo.GymPass.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SyncResult(
    @JsonProperty("users_created") int usersCreated,
    @JsonProperty("users_updated") int usersUpdated,
    @JsonProperty("memberships_created") int membershipsCreated,
    @JsonProperty("memberships_updated") int membershipsUpdated,
    @JsonProperty("sync_date") String syncDate
) {}
