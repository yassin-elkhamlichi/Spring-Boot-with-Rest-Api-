package com.codewithmosh.store.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentException extends RuntimeException {

    public PaymentException(String s) {
        super(s);
    }
}
