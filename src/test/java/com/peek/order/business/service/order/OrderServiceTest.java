package com.peek.order.business.service.order;

import com.peek.order.business.model.Customer;
import com.peek.order.business.model.Order;
import com.peek.order.business.model.Payment;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceTest {

  @Autowired private OrderService orderService;
  @Autowired private SpringDataCustomerRepository customerRepository;

  private Customer customer;

  @BeforeAll
  public void setupData() {
    customer = createCustomer("example@email.com");
  }

  @Test
  public void shouldCreateOrderWithoutPayments() {
    Order order = buildOrderModel(customer);

    var createdOrder = orderService.createOrder(order);

    assertNotNull(createdOrder.getId());
    assertEquals(BigDecimal.TEN, order.getCurrentBalance());
    assertEquals(BigDecimal.TEN, order.getValue());
    assertEquals(customer.getEmail(), order.getCustomer().getEmail());
    assertTrue(order.getPayments().isEmpty());
  }

  @Test
  public void shouldThrowExceptionWhenTryingToCreateOrderWithoutPaymentsUsingCreateOrder() {
    var order =
        Order.builder()
            .customer(customer)
            .currentBalance(BigDecimal.ZERO)
            .value(BigDecimal.TEN)
            .payments(List.of(new Payment(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN)))
            .build();

    assertThrows(
        IllegalArgumentException.class,
        () -> orderService.createOrder(order),
        "You should use create order and pay instead");
  }

  @Test
  public void
      shouldThrowExceptionWhenCurrentBalanceDoesNotMatchTheDifferenceBetweenValueAndPaymentsSum() {
    var payment1 = new Payment(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN);
    var payments = new ArrayList<Payment>();
    payments.add(payment1);
    var payment2 = new Payment(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN);
    var order =
        Order.builder()
            .customer(customer)
            .currentBalance(BigDecimal.valueOf(5))
            .value(BigDecimal.TEN)
            .payments(payments)
            .build();

    assertThrows(
        IllegalArgumentException.class,
        () -> orderService.createOrderAndPay(order, payment2),
        "Current balance must match the difference between value and payments sum");
  }

  @Test
  public void shouldApplyPaymentsToOrders(){
      var order = orderService.createOrder(buildOrderModel(customer));
      var payment = new Payment(UUID.randomUUID(),BigDecimal.TEN);

      var paidOrder = orderService.applyPaymentToOrder(order.getId(),payment);

      assertEquals(BigDecimal.ZERO.doubleValue(),paidOrder.getCurrentBalance().doubleValue());
      assertEquals(1,paidOrder.getPayments().size());
  }

  @Test
  public void shouldReturnOrderByIdWhenExists() {
    Order order = buildOrderModel(customer);
    var createdOrder = orderService.createOrder(order);

    var foundOrder = orderService.getOrder(createdOrder.getId()).get();

    assertEquals(
        createdOrder.getCurrentBalance().doubleValue(),
        foundOrder.getCurrentBalance().doubleValue());
    assertEquals(createdOrder.getCustomer().getEmail(), foundOrder.getCustomer().getEmail());
    assertEquals(createdOrder.getValue().doubleValue(), foundOrder.getValue().doubleValue());
    assertTrue(foundOrder.getPayments().isEmpty());
  }

  @Test
  public void shouldReturnEmptyOptionalWhenOrderDoesNotExist() {
    var foundOrder = orderService.getOrder(UUID.randomUUID());

    assertTrue(foundOrder.isEmpty());
  }

  @Test
  public void shouldRetrieveOrdersByCustomer() {
    buildOrderModel(customer);
    var customer2 = createCustomer("example2@email.com");
    var order1Customer2 = orderService.createOrder(buildOrderModel(customer2));
    var paymentOrder2 = new Payment( UUID.randomUUID(), BigDecimal.valueOf(9));
    var order2Customer2 = orderService.createOrderAndPay(buildOrderModel(customer2), paymentOrder2);

    var orders = orderService.getOrderForCustomer(customer2.getEmail());

    assertEquals(2, orders.size());
    assertOrderFields(order1Customer2, orders.get(0));
    assertOrderFields(order2Customer2, orders.get(1));
    // assert payments
    var paidOrderList =
        orders.stream()
            .filter(order -> order.getId().equals(order2Customer2.getId()))
            .collect(Collectors.toList());
    assertEquals(1, paidOrderList.size());
    assertEquals(9, paidOrderList.get(0).getPayments().get(0).getAmount().doubleValue());
    assertEquals(1, paidOrderList.get(0).getCurrentBalance().doubleValue());
  }

  private void assertOrderFields(Order expected, Order current) {
    assertEquals(expected.getId(), current.getId());
    assertEquals(expected.getValue().doubleValue(), current.getValue().doubleValue());
    assertEquals(
        expected.getCurrentBalance().doubleValue(), current.getCurrentBalance().doubleValue());
    assertEquals(expected.getCustomer(), current.getCustomer());
  }

  private Order buildOrderModel(Customer customer) {
    return buildOrderModel(customer, new ArrayList<>());
  }

  private Order buildOrderModel(Customer customer, List<Payment> payments) {
    return Order.builder()
        .customer(customer)
        .currentBalance(BigDecimal.TEN)
        .value(BigDecimal.TEN)
        .payments(payments)
        .build();
  }

  private Customer createCustomer(String email) {
    var customerEntity = new CustomerEntity();
    customerEntity.setEmail(email);
    return customerRepository.save(customerEntity).toCustomer();
  }
}
