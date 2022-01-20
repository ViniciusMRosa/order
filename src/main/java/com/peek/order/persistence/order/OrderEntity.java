package com.peek.order.persistence.order;

import com.peek.order.business.model.Order;
import com.peek.order.persistence.customer.CustomerEntity;
import com.peek.order.persistence.payment.PaymentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER)
    private CustomerEntity customer;
    private BigDecimal value;
    private BigDecimal currentBalance;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<PaymentEntity> payments;

    public Order toOrder(){
       var orderBuilder = Order.builder()
                .id(this.getId())
                .value(this.getValue())
                .currentBalance(this.getCurrentBalance())
                .customer(this.getCustomer().toCustomer());

       if(this.getPayments() != null){
           orderBuilder.payments(this.getPayments().stream().map(PaymentEntity::toPayment).collect(Collectors.toList()));
       }
       return orderBuilder.build();
    }

    public static OrderEntity fromOrder(Order order){
        var entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setCurrentBalance(order.getCurrentBalance());
        entity.setValue(order.getValue());
        entity.setCustomer(CustomerEntity.fromCustomer(order.getCustomer()));
        return entity;
    }
}
