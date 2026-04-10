package com.ricardo.GymPass.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Member ID is required")
    private String externalId;

    @NotBlank(message = "Password is required")
    private String password;
}