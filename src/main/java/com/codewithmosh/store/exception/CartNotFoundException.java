package com.codewithmosh.store.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(){
        super("Cart not found");
    }
}
