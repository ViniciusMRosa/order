package com.peek.order.persistence.payment;


import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPaymentRepository extends CrudRepository<PaymentEntity, UUID> {
    boolean existsByTransactionId(UUID transactionId);
}
