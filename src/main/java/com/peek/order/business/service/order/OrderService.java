package com.peek.order.business.service.order;

import com.peek.order.business.model.Order;
import com.peek.order.business.model.Payment;
import com.peek.order.business.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private OrderRepository repository;
    private PaymentService paymentService;

    @Autowired
    public OrderService(OrderRepository repository, PaymentService paymentService) {
        this.repository = repository;
        this.paymentService = paymentService;
    }

    @Transactional
    public Order createOrder(Order order){
        if(order.getPayments() != null && !order.getPayments().isEmpty()){
            throw new IllegalArgumentException("You should use create order and pay instead");
        }
        validateOrderData(order);
        return repository.save(order);
    }

    @Transactional
    public Order createOrderAndPay(Order order, Payment payment){
        order.addPayment(payment);
        validateOrderData(order);

        var createdOrder = repository.save(order);
        paymentService.applyPayment(createdOrder,payment);
        return repository.getOrder(createdOrder.getId()).get();
    }

    @Transactional
    public Order applyPaymentToOrder(UUID orderId, Payment payment) throws IllegalArgumentException{
        var order = repository.getOrder(orderId).orElseThrow(() -> new IllegalArgumentException("Order does not exist"));
        order.addPayment(payment);
        validateOrderData(order);
        paymentService.applyPayment(order,payment);

        return repository.save(order);
    }

    private void validateOrderData(Order order){
        if(order.getCurrentBalance().compareTo(BigDecimal.ZERO) == -1){
            throw new IllegalArgumentException("Current balance must be less or equal total order value");
        }
    }

    public Optional<Order> getOrder(UUID id) {
        return repository.getOrder(id);
    }

    public List<Order> getOrderForCustomer(String email){
        return repository.getOrderForCustomer(email);
    }
}
