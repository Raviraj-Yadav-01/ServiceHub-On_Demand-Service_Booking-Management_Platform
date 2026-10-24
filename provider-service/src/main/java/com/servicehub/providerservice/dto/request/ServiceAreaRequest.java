package com.servicehub.providerservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServiceAreaRequest {
    @NotBlank(message = "Pincode is required")
    private String pincode;

    @NotBlank(message = "City name is required")
    private String cityName;

    private String areaName;
}