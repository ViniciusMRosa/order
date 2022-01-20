package com.peek.order.business.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Payment {
    private UUID id;
    private UUID transactionId;
    private BigDecimal amount;

    public Payment(UUID id, UUID transactionId, BigDecimal amount) {
        this.id = id;
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public Payment(UUID transactionId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }
}
