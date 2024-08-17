package com.toy2.shop29.cart.exception;

import com.toy2.shop29.exception.error.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {

    public ProductNotFoundException(String msg) {
        super(msg);
    }
}


