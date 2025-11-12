package com.codewithmosh.store.payement;

import com.codewithmosh.store.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private Status paymentStatus;
}
