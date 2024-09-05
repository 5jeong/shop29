package com.toy2.shop29.order.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDto {
    @NotNull()
    public Long productId;
    @NotNull()
    public Long quantity;
    @NotNull()
    public Long productOptionId;
}
