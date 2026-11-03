package com.servicehub.paymentservice.controller;

import com.servicehub.paymentservice.dto.request.PaymentRequestDto;
import com.servicehub.paymentservice.dto.response.PaymentResponseDto;
import com.servicehub.paymentservice.entity.Payment;
import com.servicehub.paymentservice.mapper.PaymentMapper;
import com.servicehub.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPayment(@Valid @RequestBody PaymentRequestDto request) {
        Payment savedPayment = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentMapper.toResponseDto(savedPayment));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByBookingId(@PathVariable Long bookingId) {
        Payment payment = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(paymentMapper.toResponseDto(payment));
    }
}