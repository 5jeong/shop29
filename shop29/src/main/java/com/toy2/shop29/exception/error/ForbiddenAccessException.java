package com.toy2.shop29.exception.error;

import com.toy2.shop29.exception.BusinessException;

public class ForbiddenAccessException extends BusinessException {
    public ForbiddenAccessException() {
        super(ErrorCode.FORBIDDEN_ACCESS);
    }
}
