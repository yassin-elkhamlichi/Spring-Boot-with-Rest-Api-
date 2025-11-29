package com.yassineproject.store.payement;

import com.yassineproject.store.order.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private Status paymentStatus;
}
