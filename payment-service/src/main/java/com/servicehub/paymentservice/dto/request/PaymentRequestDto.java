package com.servicehub.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {

    @NotNull(message = "Booking ID is mandatory")
    private Long bookingId;

    @NotNull(message = "Customer ID is mandatory")
    private Long customerId;

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "1.00", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is mandatory (e.g., UPI, CARD)")
    private String paymentMethod;
}