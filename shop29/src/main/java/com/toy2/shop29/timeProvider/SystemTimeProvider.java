package com.toy2.shop29.timeProvider;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class SystemTimeProvider implements TimeProvider{
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
