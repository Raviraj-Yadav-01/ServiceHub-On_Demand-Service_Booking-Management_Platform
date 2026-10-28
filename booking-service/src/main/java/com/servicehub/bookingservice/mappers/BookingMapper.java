package com.servicehub.bookingservice.mappers;

import com.servicehub.bookingservice.dto.response.BookingResponseDto;
import com.servicehub.bookingservice.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponseDto toResponseDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomerId());
        dto.setProviderId(booking.getProviderId());
        dto.setServiceCategoryId(booking.getServiceCategoryId());
        dto.setCustomerAddressId(booking.getCustomerAddressId());
        dto.setScheduledDate(booking.getScheduledDate());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setProblemDescription(booking.getProblemDescription());
        dto.setCreatedAt(booking.getCreatedAt());

        return dto;
    }
}