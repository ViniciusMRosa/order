package com.peek.order.persistence.payment;

import com.peek.order.business.model.Order;
import com.peek.order.business.model.Payment;
import com.peek.order.business.service.payment.PaymentRepository;
import com.peek.order.persistence.order.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class InMemoryPaymentRepository implements PaymentRepository {
    private SpringDataPaymentRepository repository;

    @Autowired
    public InMemoryPaymentRepository(SpringDataPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment applyPayment(Order order, Payment payment) {
        var orderEntity = OrderEntity.fromOrder(order);

        return  repository.save(PaymentEntity.fromPayment(orderEntity,payment)).toPayment();
    }

    @Override
    public boolean existsByTransactionId(UUID transactionId) {
        return repository.existsByTransactionId(transactionId);
    }
}
