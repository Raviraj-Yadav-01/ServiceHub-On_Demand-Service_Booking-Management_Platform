package com.servicehub.bookingservice.repository;

import com.servicehub.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.providerId = :providerId " +
            "AND b.scheduledDate = :scheduledDate " +
            "AND b.status != 'CANCELLED' " +
            "AND (b.startTime < :endTime AND b.endTime > :startTime)")
    boolean existsConflictingBooking(
            @Param("providerId") Long providerId,
            @Param("scheduledDate") LocalDate scheduledDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByProviderId(Long providerId);

}