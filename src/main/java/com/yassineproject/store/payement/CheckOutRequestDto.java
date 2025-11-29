package com.yassineproject.store.payement;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckOutRequestDto {
    @NotNull(message = "Cart id is required")
    private UUID cartId;
}
