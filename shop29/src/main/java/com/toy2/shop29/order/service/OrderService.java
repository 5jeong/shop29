package com.toy2.shop29.order.service;

import com.toy2.shop29.order.domain.OrderItemDTO;
import com.toy2.shop29.order.domain.ShippingAddressInfoDTO;
import com.toy2.shop29.order.domain.request.OrderProductDto;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;
import com.toy2.shop29.order.domain.response.OrderHistoryDTO;
import com.toy2.shop29.order.domain.response.OrderPageResponseDTO;

import java.util.List;

public interface OrderService {
    int checkUserCurrentOrderExist(String userId) throws Exception;

    int createUserCurrentOrder(String userId) throws Exception;

    List<OrderItemDTO> getOrderItems(String userId) throws Exception;

    int addProductToCurrentOrder(String userId, Long productId, Long quantity) throws Exception;

    int updateProductsToCurrentOrder(String userId, Long productId, Long quantity) throws Exception;

    int addProducts(String userId, List<OrderProductDto> products) throws Exception;

    int deleteUserAllProducts(String userId) throws Exception;

    OrderPageResponseDTO getCurrentOrderInfo(String userId) throws Exception;

    int deleteCurrentOrder(String userId) throws Exception;

    int orderProcess(String userId, String tid, OrderCompletedRequestDTO orderRequest) throws Exception;

    int createOrderHistory(String orderId, String userId, String tid, Long totalPrice, Long shippingAddressId) throws Exception;

    Long getProductPrice(Long productId);

    int addUserOrderHistoryItem(String orderId, String userId, Long productId, Long quantity) throws Exception;

    int insertOrderAddress(String orderId, String userId, ShippingAddressInfoDTO orderAddress) throws Exception;

    String selectProductNameByProductId(Long productId);

    int updateUserOrderStatus(String userId, String tid, String orderStatus) throws Exception;

    List<OrderHistoryDTO> getOrderHistory(String userId) throws Exception;

    void deleteOrderHistory(String userId, String tid) throws Exception;

    int deletePayFailedOrderHistoryItem(String userId, String tid) throws Exception;

    int deletePayFailedOrderAddress(String userId, String tid) throws Exception;

    int deletePayFailedOrderHistory(String userId, String tid) throws Exception;

    int countOrderHistoryByIdTid(String userId, String tid) throws Exception;

    int countUserOrderAddress(String userId, String tid) throws Exception;

    int countUserOrderHistoryItem(String userId, String tid) throws Exception;
}
