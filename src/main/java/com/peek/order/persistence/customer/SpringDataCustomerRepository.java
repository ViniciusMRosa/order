package com.peek.order.persistence.customer;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SpringDataCustomerRepository extends CrudRepository<CustomerEntity, UUID> {
}
