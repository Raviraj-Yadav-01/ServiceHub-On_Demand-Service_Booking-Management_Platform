package com.servicehub.paymentservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEventDto {
    private Long bookingId;
    private Long customerId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private String status;
}
