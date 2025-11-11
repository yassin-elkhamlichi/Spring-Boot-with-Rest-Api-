package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutDto {
    @NotNull(message = "CartId is required")
    private UUID CartId;
}
