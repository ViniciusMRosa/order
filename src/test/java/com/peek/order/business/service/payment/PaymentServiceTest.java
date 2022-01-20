package com.peek.order.business.service.payment;

import com.peek.order.business.model.Customer;
import com.peek.order.business.model.Order;
import com.peek.order.business.model.Payment;
import com.peek.order.business.service.order.OrderService;
import com.peek.order.persistence.customer.CustomerEntity;
import com.peek.order.persistence.customer.SpringDataCustomerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentServiceTest {

  @Autowired private PaymentService paymentService;
  @Autowired private OrderService orderService;
  @Autowired private SpringDataCustomerRepository customerRepository;
  private Customer customer;

  @BeforeAll
  public void setupData() {
    var customerEntity = new CustomerEntity();
    customerEntity.setEmail("example-payment@email.com");
    customer = customerRepository.save(customerEntity).toCustomer();
  }

  @Test
  public void shouldCreatePayment() {
    var order =
        Order.builder()
            .customer(customer)
            .currentBalance(BigDecimal.TEN)
            .value(BigDecimal.TEN)
            .payments(List.of())
            .build();

    var createdOrder = orderService.createOrder(order);
    var transactionId = UUID.randomUUID();
    var payment = new Payment(transactionId, BigDecimal.TEN);

    var createdPayment = paymentService.applyPayment(createdOrder, payment);

    assertNotNull(createdPayment.getId());
    assertEquals(transactionId, createdPayment.getTransactionId());
    assertEquals(BigDecimal.TEN, createdPayment.getAmount());

    var orderWithPayments = orderService.getOrder(createdOrder.getId()).get();
    assertEquals(1, orderWithPayments.getPayments().size());
    assertEquals(
        BigDecimal.TEN.doubleValue(),
        orderWithPayments.getPayments().get(0).getAmount().doubleValue());
  }

  @Test
  public void shouldThrowExceptionWhenPaymentAlreadyExists() {
    var order =
        Order.builder()
            .customer(customer)
            .currentBalance(BigDecimal.TEN)
            .value(BigDecimal.TEN)
            .payments(List.of())
            .build();

    var createdOrder = orderService.createOrder(order);
    var transactionId = UUID.randomUUID();
    var payment = new Payment(transactionId, BigDecimal.TEN);
    var payment2 = new Payment(transactionId, BigDecimal.TEN);

    paymentService.applyPayment(createdOrder, payment);

    assertThrows(
        IllegalArgumentException.class,
        () -> paymentService.applyPayment(createdOrder, payment2),
        "\"Payment already applied to this order\"");
  }
}
