package com.toy2.shop29.cart.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartProductDto {
    @NotNull(message = "상품 ID 필요")
    private Long productId;
    @NotNull(message = "수량은 필수 입력 항목입니다.")
    @Min(value = 1, message = "수량은 최소 1이어야 합니다.")
    @Max(value = 100, message = "수량은 최대 100이어야 합니다.")
    private Long quantity;
}
