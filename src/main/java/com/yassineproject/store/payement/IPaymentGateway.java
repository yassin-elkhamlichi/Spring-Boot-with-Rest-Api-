package com.yassineproject.store.payement;

import com.yassineproject.store.order.Orders;

import java.util.Optional;

public interface IPaymentGateway {
    CheckoutSession createCheckoutSession(Orders order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
