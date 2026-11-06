package com.servicehub.paymentservice.service;

import com.servicehub.paymentservice.client.BookingClient;
import com.servicehub.paymentservice.dto.request.PaymentRequestDto;
import com.servicehub.paymentservice.dto.response.BookingResponseDto;
import com.servicehub.paymentservice.entity.Payment;
import com.servicehub.paymentservice.entity.PaymentStatus;
import com.servicehub.paymentservice.kafka.KafkaProducerService;
import com.servicehub.paymentservice.kafka.PaymentEventDto;
import com.servicehub.paymentservice.repository.PaymentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingClient bookingClient;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
   // @CircuitBreaker(name = "bookingService", fallbackMethod = "processPaymentFallback")
    public Payment processPayment(PaymentRequestDto request) {

        paymentRepository.findByBookingId(request.getBookingId()).ifPresent(p -> {
            throw new IllegalStateException("Payment already processed for booking ID: " + request.getBookingId());
        });

        List<BookingResponseDto> customerBookings = bookingClient.getBookingsByCustomerId(request.getCustomerId());

        if (customerBookings == null || customerBookings.isEmpty()) {
            throw new IllegalArgumentException("No bookings found for this customer.");
        }

        BookingResponseDto targetBooking = customerBookings.stream()
                .filter(b -> b.getId().equals(request.getBookingId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Booking ID " + request.getBookingId() + " does not belong to this customer or does not exist."));


        if (targetBooking.getTotalAmount().compareTo(request.getAmount()) != 0) {
            throw new IllegalArgumentException("Amount mismatch! Expected: " + targetBooking.getTotalAmount() + ", but got: " + request.getAmount());
        }


        if ("CONFIRMED".equals(targetBooking.getStatus()) || "CANCELLED".equals(targetBooking.getStatus())) {
            throw new IllegalStateException("Payment cannot be processed for " + targetBooking.getStatus() + " bookings.");
        }


        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setCustomerId(request.getCustomerId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId("TXN_" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());

        Payment savedPayment = paymentRepository.save(payment);

        PaymentEventDto event = new PaymentEventDto(
                savedPayment.getBookingId(),
                savedPayment.getCustomerId(),
                savedPayment.getAmount(),
                savedPayment.getPaymentMethod(),
                savedPayment.getTransactionId(),
                savedPayment.getStatus().name()
        );


        kafkaProducerService.sendPaymentSuccessEvent(event);


        try {
            bookingClient.updateBookingStatus(request.getBookingId(), "CONFIRMED");
            System.out.println("Booking status update successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Payment successful, but failed to update Booking status. Error: " + e.getMessage());
        }

        return savedPayment; // Fixed: using savedPayment
    }

    // NAYA LOGIC: Fallback Method (Jab Booking Service Down Ho)
    public Payment processPaymentFallback(PaymentRequestDto request, Throwable t) {
        System.err.println("🚨 CIRCUIT BREAKER TRIGGERED! Booking Service is down. Reason: " + t.getMessage());

        // Kyunki hum booking details (amount etc.) verify nahi kar sakte,
        // hum payment fail kar denge gracefully bina system crash kiye.
        Payment fallbackPayment = new Payment();
        fallbackPayment.setBookingId(request.getBookingId());
        fallbackPayment.setCustomerId(request.getCustomerId());
        fallbackPayment.setAmount(request.getAmount());
        fallbackPayment.setPaymentMethod(request.getPaymentMethod());
        fallbackPayment.setStatus(PaymentStatus.FAILED); // Status Failed mark kiya
        fallbackPayment.setTransactionId("FAILED_SYSTEM_DOWN");

        // Database mein save nahi karenge, bas API response gracefully return karenge
        return fallbackPayment;
    }

    public Payment getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking ID: " + bookingId));
    }
}