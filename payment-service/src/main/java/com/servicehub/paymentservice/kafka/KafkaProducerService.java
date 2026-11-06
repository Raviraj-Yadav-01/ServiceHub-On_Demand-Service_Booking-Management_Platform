package com.servicehub.paymentservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // application.properties wali value yahan aayegi
    @Value("${kafka.topic.payment-notification}")
    private String paymentTopic;

    public void sendPaymentSuccessEvent(PaymentEventDto event) {
        kafkaTemplate.send(paymentTopic, event);
        System.out.println("🚀 Kafka Event Sent -> Payment Success for Booking ID: " + event.getBookingId());
    }
}