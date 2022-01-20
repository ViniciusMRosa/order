package com.peek.order.persistence.order;

import com.peek.order.business.model.Order;
import com.peek.order.business.service.order.OrderRepository;
import com.peek.order.persistence.customer.SpringDataCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class InMemoryOrderRepository implements OrderRepository {
    private SpringDataOrderRepository orderRepository;
    private SpringDataCustomerRepository customerRepository;

    @Autowired
    public InMemoryOrderRepository(SpringDataOrderRepository orderRepository, SpringDataCustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public Order save(Order order){
        var customerEntity = customerRepository.findById(order.getCustomer().getId());

        var orderEntity = OrderEntity.builder()
                .currentBalance(order.getCurrentBalance())
                .value(order.getValue())
                .customer(customerEntity.get())
                .build();

        var persistedOrder = orderRepository.save(orderEntity);

        return persistedOrder.toOrder();
    }

    @Override
    public Optional<Order> getOrder(UUID id) {
        return orderRepository.findById(id).map(OrderEntity::toOrder);
    }

    @Override
    public List<Order> getOrderForCustomer(String email) {
        return orderRepository.findByCustomer(email).stream().map(OrderEntity::toOrder).collect(Collectors.toList());
    }

}
