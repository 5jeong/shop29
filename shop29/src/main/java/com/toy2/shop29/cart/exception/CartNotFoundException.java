package com.toy2.shop29.cart.exception;

import com.toy2.shop29.exception.error.EntityNotFoundException;

public class CartNotFoundException extends EntityNotFoundException {

    public CartNotFoundException(String msg) {
        super(msg);
    }
}
