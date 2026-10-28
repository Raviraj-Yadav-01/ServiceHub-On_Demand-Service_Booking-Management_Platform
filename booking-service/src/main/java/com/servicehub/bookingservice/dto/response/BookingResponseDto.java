package com.servicehub.bookingservice.dto.response;

import com.servicehub.bookingservice.entity.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class BookingResponseDto {
    private Long id;
    private Long customerId;
    private Long providerId;
    private Long serviceCategoryId;
    private Long customerAddressId;
    private LocalDate scheduledDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private String problemDescription;
    private LocalDateTime createdAt;
}