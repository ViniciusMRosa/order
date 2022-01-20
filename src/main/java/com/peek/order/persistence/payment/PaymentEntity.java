package com.peek.order.persistence.payment;

import com.peek.order.business.model.Order;
import com.peek.order.business.model.Payment;
import com.peek.order.persistence.order.OrderEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(of ="id")
public class PaymentEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private BigDecimal amount;
    private UUID transactionId;
    @ManyToOne
    private OrderEntity order;

    public Payment toPayment(){
        return new Payment(this.getId(),this.getTransactionId(),this.getAmount());
    }

    public static PaymentEntity fromPayment(OrderEntity orderEntity,Payment payment){
        var entity =  new PaymentEntity();
        entity.setId(payment.getId());
        entity.setTransactionId(payment.getTransactionId());
        entity.setAmount(payment.getAmount());
        entity.setOrder(orderEntity);
        return entity;
    }
}
