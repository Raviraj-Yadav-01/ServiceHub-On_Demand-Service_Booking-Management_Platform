package com.servicehub.providerservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ProviderProfileResponse {
    private Long id;
    private String businessName;
    private String contactEmail;
    private String contactPhone;
    private String bio;
    private Integer experienceYears;
    private String verificationStatus;
    private LocalDateTime createdAt;
}