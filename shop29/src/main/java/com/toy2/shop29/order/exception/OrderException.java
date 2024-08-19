package com.toy2.shop29.order.exception;

import com.toy2.shop29.exception.BusinessException;
import com.toy2.shop29.exception.error.ErrorCode;

public class OrderException extends BusinessException {
    public OrderException(ErrorCode errorCode) {
        super(errorCode);
    }
}
