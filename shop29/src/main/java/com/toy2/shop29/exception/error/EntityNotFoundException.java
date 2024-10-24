package com.toy2.shop29.exception.error;

import com.toy2.shop29.exception.BusinessException;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
