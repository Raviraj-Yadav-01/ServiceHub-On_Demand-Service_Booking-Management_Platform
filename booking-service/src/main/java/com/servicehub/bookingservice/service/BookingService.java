package com.servicehub.bookingservice.service;

import com.servicehub.bookingservice.dto.request.BookingRequestDto;
import com.servicehub.bookingservice.entity.Booking;
import com.servicehub.bookingservice.entity.BookingStatus;
import com.servicehub.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    public Booking createBooking(BookingRequestDto request) {
        // 1. Basic Time Rules
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        // 2. Double Booking Check (Concurrency Prevention)
        boolean hasConflict = bookingRepository.existsConflictingBooking(
                request.getProviderId(),
                request.getScheduledDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (hasConflict) {
            throw new IllegalStateException("The provider is already booked for this time slot.");
        }

        // 3. Map DTO to Entity
        Booking booking = new Booking();
        booking.setCustomerId(request.getCustomerId());
        booking.setProviderId(request.getProviderId());
        booking.setServiceCategoryId(request.getServiceCategoryId());
        booking.setCustomerAddressId(request.getCustomerAddressId());
        booking.setScheduledDate(request.getScheduledDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setTotalAmount(request.getTotalAmount());
        booking.setProblemDescription(request.getProblemDescription());
        booking.setStatus(BookingStatus.PENDING); // Default status

        // 4. Save and return
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));

        // Strict state transition rules (Rule 11)
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a cancelled booking.");
        }

        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public List<Booking> getBookingsByProvider(Long providerId) {
        return bookingRepository.findByProviderId(providerId);
    }
}