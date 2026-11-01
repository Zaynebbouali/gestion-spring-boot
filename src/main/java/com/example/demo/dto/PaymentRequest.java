package com.example.demo.dto;

import java.math.BigDecimal;

import com.example.demo.Entity.enums.PaymentMethod;
import com.example.demo.Entity.enums.PaymentStatus;

/** Données envoyées pour créer / modifier un paiement (vérifiées dans PaymentService). */
public record PaymentRequest(
		BigDecimal amount,
		PaymentMethod method,
		PaymentStatus status,
		Long reservationId
) {}
