package com.servicehub.paymentservice.dto.response;


import lombok.Data;
import java.math.BigDecimal;

// Ye DTO sirf Booking Service se data receive karne ke liye hai
@Data
public class BookingResponseDto {
    private Long id;
    private Long customerId;
    private BigDecimal totalAmount;
    private String status;
}
