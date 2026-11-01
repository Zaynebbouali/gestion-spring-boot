package com.example.demo.dto;

import com.example.demo.Entity.Payment;
import com.example.demo.Entity.enums.PaymentMethod;
import com.example.demo.Entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        LocalDateTime paymentDate,
        Long reservationId
) {
    public static PaymentResponse from(Payment p) {
        return new PaymentResponse(p.getId(), p.getAmount(), p.getMethod(), p.getStatus(),
                p.getPaymentDate(), p.getReservation().getId());
    }
}
