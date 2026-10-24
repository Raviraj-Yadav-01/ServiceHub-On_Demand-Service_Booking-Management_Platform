package com.servicehub.providerservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ServiceAreaResponse {
    private Long id;
    private String pincode;
    private String cityName;
    private String areaName;
    private LocalDateTime createdAt;
}