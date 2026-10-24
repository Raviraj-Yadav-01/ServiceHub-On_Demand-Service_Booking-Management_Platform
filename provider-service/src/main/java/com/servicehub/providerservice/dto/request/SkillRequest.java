package com.servicehub.providerservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SkillRequest {
    @NotBlank(message = "Category is required (e.g., Electrician)")
    private String category;

    @NotBlank(message = "Skill name is required")
    private String skillName;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;
}