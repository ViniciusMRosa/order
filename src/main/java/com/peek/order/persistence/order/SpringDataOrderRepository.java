package com.peek.order.persistence.order;

import com.peek.order.business.model.Order;
import com.peek.order.persistence.order.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataOrderRepository extends CrudRepository<OrderEntity, UUID> {

    @Query("select o from OrderEntity o where o.customer.email = :email")
    List<OrderEntity> findByCustomer(@Param("email") String email);
}
