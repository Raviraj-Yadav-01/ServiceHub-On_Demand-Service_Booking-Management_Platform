package com.servicehub.providerservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class AvailabilityRequest {
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "isAvailable flag is required")
    private Boolean isAvailable;

    // Optional because if isAvailable = false, times are not needed
    private LocalTime startTime;
    private LocalTime endTime;
}