package com.codewithmosh.store.order;

import com.codewithmosh.store.users.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    @JsonIgnore
    private UserDto user;
    private Status status;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private Set<OrderItemDto> items = new HashSet<>();
}
