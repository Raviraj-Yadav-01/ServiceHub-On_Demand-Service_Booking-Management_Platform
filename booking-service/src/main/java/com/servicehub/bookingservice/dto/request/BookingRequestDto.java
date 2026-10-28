package com.servicehub.bookingservice.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequestDto {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "Service Category ID is required")
    private Long serviceCategoryId;

    @NotNull(message = "Customer Address ID is required")
    private Long customerAddressId;

    @NotNull(message = "Scheduled date is required")
    @FutureOrPresent(message = "Scheduled date cannot be in the past")
    private LocalDate scheduledDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be greater than zero")
    private BigDecimal totalAmount;

    @Size(max = 1000, message = "Problem description cannot exceed 1000 characters")
    private String problemDescription;
}