package com.peek.order.business.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class Customer {
    private UUID id;
    private String email;
}
