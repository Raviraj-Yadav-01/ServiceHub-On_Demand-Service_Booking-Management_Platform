package com.servicehub.notificationservice.kafka;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentEventDto {
    private Long bookingId;
    private Long customerId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private String status;
}
