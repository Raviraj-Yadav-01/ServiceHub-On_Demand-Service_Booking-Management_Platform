package com.servicehub.paymentservice.client;

import com.servicehub.paymentservice.dto.response.BookingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// name = "booking-service" wahi naam hona chahiye jo Eureka mein register hota hai
@FeignClient(name = "booking-service")
public interface BookingClient {

    @GetMapping("/api/v1/bookings/customer/{id}")
    List<BookingResponseDto> getBookingsByCustomerId(@PathVariable("id") Long customerId);


    // NAYA METHOD: Booking status update karne ke liye
    @PatchMapping("/api/v1/bookings/{id}/status")
    void updateBookingStatus(@PathVariable("id") Long id, @RequestParam("status") String status);
}
