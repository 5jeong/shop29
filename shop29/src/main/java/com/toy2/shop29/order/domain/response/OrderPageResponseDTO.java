package com.toy2.shop29.order.domain.response;

import com.toy2.shop29.order.domain.CurrentOrderDTO;
import com.toy2.shop29.order.domain.OrderItemDTO;
import com.toy2.shop29.order.domain.ShippingAddressInfoDTO;

import java.util.List;

public class OrderPageResponseDTO {
    private ShippingAddressInfoDTO shippingAddress;
    private List<OrderItemDTO> currentItemsData;

    public ShippingAddressInfoDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressInfoDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemDTO> getCurrentItemsData() {
        return currentItemsData;
    }

    public void setCurrentItemsData(List<OrderItemDTO> currentItemsData) {
        this.currentItemsData = currentItemsData;
    }
}
