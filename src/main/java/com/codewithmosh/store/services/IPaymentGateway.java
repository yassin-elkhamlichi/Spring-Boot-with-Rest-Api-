package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Orders;

public interface IPaymentGateway {
    CheckoutSession createCheckoutSession(Orders order);
}
