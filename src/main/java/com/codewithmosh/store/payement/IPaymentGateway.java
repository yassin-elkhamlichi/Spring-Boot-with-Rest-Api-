package com.codewithmosh.store.payement;

import com.codewithmosh.store.order.Orders;

import java.util.Optional;

public interface IPaymentGateway {
    CheckoutSession createCheckoutSession(Orders order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
