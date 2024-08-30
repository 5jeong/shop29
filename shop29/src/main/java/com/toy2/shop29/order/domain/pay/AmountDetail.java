package com.toy2.shop29.order.domain.pay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmountDetail {
    private Long total;
    private Long tax_free;
    private Long vat;
    private Long point;
    private Long discount;
    private Long green_deposit;
}
