package com.servicehub.providerservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProviderProfileRequest {
    @NotBlank(message = "Business name is required")
    private String businessName;

    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    private String contactPhone;

    private String bio;

    @NotNull(message = "Experience years is required")
    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experienceYears;
}