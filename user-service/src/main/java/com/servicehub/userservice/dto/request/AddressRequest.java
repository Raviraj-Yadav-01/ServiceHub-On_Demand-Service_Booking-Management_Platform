package com.servicehub.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddressRequest {

    @NotBlank(message = "Address label is required (e.g. Home, Work)")
    private String addressLabel;

    private String recipientName;
    private String recipientPhone;

    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;

    private String addressLine2;
    private String landmark;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @NotNull
    private Boolean isDefault = false;
}