package com.peek.order.business.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Order {
    private UUID id;
    private Customer customer;
    private BigDecimal value;
    private BigDecimal currentBalance;
    private List<Payment> payments;

    public void calculateBalance(){
        final BigDecimal subtract = this.value.subtract(payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        this.currentBalance = subtract;
    }

    public void addPayment(Payment payment){
        this.payments.add(payment);
        calculateBalance();
    }
}
