package com.peek.order.persistence.customer;

import com.peek.order.business.model.Customer;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class CustomerEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String email;

    public final Customer toCustomer(){
        return new Customer(this.getId(),this.getEmail());
    }

    public static CustomerEntity fromCustomer(Customer customer){
        var entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setEmail(customer.getEmail());
        return entity;
    }
}
