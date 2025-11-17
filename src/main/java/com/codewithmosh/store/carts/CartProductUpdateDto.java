package com.codewithmosh.store.carts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
public class CartProductUpdateDto {
    @Min(value = 1, message = "Quantity cannot be less than 1")
    @Max(value = 99 , message = "Quantity cannot be more than 99")
    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;
}
