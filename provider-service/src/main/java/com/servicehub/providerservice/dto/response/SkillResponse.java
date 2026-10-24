package com.servicehub.providerservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SkillResponse {
    private Long id;
    private String category;
    private String skillName;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;
}