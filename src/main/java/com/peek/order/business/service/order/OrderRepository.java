package com.peek.order.business.service.order;

import com.peek.order.business.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> getOrder(UUID id);
    List<Order> getOrderForCustomer(String email);
}
