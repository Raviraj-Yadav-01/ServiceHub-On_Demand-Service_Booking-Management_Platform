package com.servicehub.providerservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
public class AvailabilityResponse {
    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
}