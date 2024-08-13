package com.toy2.shop29.cart.domain.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {
    @NotBlank(message = "비정상적인 접근입니다.")
    private Long productId;
    @NotBlank(message = "비정상적인 접근입니다.")
    private Long quantity;
    private String productName;
    private Long price;
    private Long saleRatio;
    private String sizeInfo;
}
