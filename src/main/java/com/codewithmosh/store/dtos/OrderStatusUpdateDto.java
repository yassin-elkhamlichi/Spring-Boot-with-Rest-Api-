package com.codewithmosh.store.dtos;

import com.codewithmosh.store.entities.Status;
import lombok.Data;

@Data
public class OrderStatusUpdateDto {
    private Status status;
}
