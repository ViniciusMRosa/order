### Simple Order Management


## Problem solved
 In order to allow our internal customers to create orders we have created an API that can be used by them. This API has the main features for an order creation, as listed bellow.
 
*Features*
 - Create unpaid orders
 - Create paid orders
 - Add payments for existent orders
 - List orders for a customer
 - Find order data along with its payments
 

### How to use

This API provides a Facade called Order Service that consists in all allowed methods

`createOrder(Order order)`  

Creates an order without payments. If you want to add payments refer to `Order createOrderAndPay(Order order, Payment payment)` 


`Order createOrderAndPay(Order order, Payment payment)`

Creates an order and a payment for it. It can be the full payment or only and installment.

`Order applyPaymentToOrder(UUID orderId, Payment payment)`

Apply a payment to an existing order, if the provided order does not exist it throws exception

`Optional<Order> getOrder`

Gets an order by its id. If the order does not exist it returns empty.

`List<Order> getOrderForCustomer(String email)`

Gets a list of orders for a customer based on a customer's email. It returns an empty list when no orders found.


### Future enhancements

- Fix the bug that is causing `shouldApplyPaymentsToOrders()` test case to fail 
- Enhance exception handling creating specific exceptions related to business rules


### Architecture

I have tried to use a lightweight onion architecture, separated by packages instead of modules

### Technology used
 - Java
 - Spring Boot
 - Spring Data JPA
 - H2 in memory Database
 - Lombok

