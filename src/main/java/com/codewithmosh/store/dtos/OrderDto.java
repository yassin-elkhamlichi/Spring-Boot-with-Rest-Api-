package com.codewithmosh.store.dtos;

import com.codewithmosh.store.entities.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {
    private Long id;
    private UserDto user;
    private Status status;
    private LocalDateTime orderDate;
    private Double totalAmount;
}
