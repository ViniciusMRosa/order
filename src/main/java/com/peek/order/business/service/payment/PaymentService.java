package com.peek.order.business.service.payment;

import com.peek.order.business.model.Order;
import com.peek.order.business.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
  private PaymentRepository repository;

  @Autowired
  public PaymentService(PaymentRepository repository) {
    this.repository = repository;
  }

  public Payment applyPayment(Order order, Payment payment) {
    if (repository.existsByTransactionId(payment.getTransactionId())) {
      throw new IllegalArgumentException("Payment already applied");
    }
    return repository.applyPayment(order, payment);
  }
}
