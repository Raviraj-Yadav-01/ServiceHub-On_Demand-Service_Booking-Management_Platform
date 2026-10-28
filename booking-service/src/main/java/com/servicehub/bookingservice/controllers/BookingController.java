package com.servicehub.bookingservice.controllers;

import com.servicehub.bookingservice.dto.request.BookingRequestDto;
import com.servicehub.bookingservice.dto.response.BookingResponseDto;
import com.servicehub.bookingservice.entity.Booking;
import com.servicehub.bookingservice.entity.BookingStatus;
import com.servicehub.bookingservice.mappers.BookingMapper;
import com.servicehub.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {


    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto request) {
        Booking savedBooking = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingMapper.toResponseDto(savedBooking));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponseDto> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        Booking updatedBooking = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(bookingMapper.toResponseDto(updatedBooking));
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByCustomer(@PathVariable Long customerId) {
        List<Booking> bookings = bookingService.getBookingsByCustomer(customerId);
        List<BookingResponseDto> responseDtos = bookings.stream()
                .map(bookingMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByProvider(@PathVariable Long providerId) {
        List<Booking> bookings = bookingService.getBookingsByProvider(providerId);
        List<BookingResponseDto> responseDtos = bookings.stream()
                .map(bookingMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(responseDtos);
    }
}