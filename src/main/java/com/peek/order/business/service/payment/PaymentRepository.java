package com.peek.order.business.service.payment;

import com.peek.order.business.model.Order;
import com.peek.order.business.model.Payment;

import java.util.UUID;

public interface PaymentRepository {
    Payment applyPayment(Order order, Payment payment);

    boolean existsByTransactionId(UUID transactionId);
}
