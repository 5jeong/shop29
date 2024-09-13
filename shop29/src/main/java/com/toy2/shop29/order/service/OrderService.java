package com.toy2.shop29.order.service;

import com.toy2.shop29.common.ProductItem;
import com.toy2.shop29.order.domain.OrderHistoryDTO;
import com.toy2.shop29.order.domain.OrderItemDTO;
import com.toy2.shop29.order.domain.ShippingAddressInfoDTO;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;
import com.toy2.shop29.order.domain.request.OrderProductDto;
import com.toy2.shop29.order.domain.response.OrderHistoryResponseDTO;
import com.toy2.shop29.order.domain.response.OrderPageResponseDTO;

import java.util.List;

public interface OrderService {
    int checkUserCurrentOrderExist(String userId) throws Exception;

    int createUserCurrentOrder(String userId) throws Exception;

    List<OrderItemDTO> getCurrentOrderItems(String userId) throws Exception;

    int addProductToCurrentOrder(String userId, Long productId, Long quantity, Long productOptionId) throws Exception;

    int updateProductsToCurrentOrder(String userId, Long productId, Long quantity) throws Exception;

    int addProducts(String userId, List<OrderProductDto> products) throws Exception;

    int deleteUserAllProducts(String userId) throws Exception;

    OrderPageResponseDTO getCurrentOrderInfo(String userId) throws Exception;

    int deleteCurrentOrder(String userId) throws Exception;

    int orderProcess(String userId, String tid, OrderCompletedRequestDTO orderRequest) throws Exception;

    int createOrderHistory(String orderId, String userId, String tid, Long totalPrice, Long shippingAddressId) throws Exception;

    Long getProductPrice(Long productId);

    int addUserOrderHistoryItem(String orderId, String userId, Long productId, Long quantity, Long productOptionId, Long productPrice) throws Exception;

    int insertOrderAddress(String orderId, String userId, ShippingAddressInfoDTO orderAddress) throws Exception;

    String selectProductNameByProductId(Long productId);

    int updateUserOrderStatus(String userId, String tid, String orderStatus) throws Exception;

    List<OrderHistoryResponseDTO> getOrderHistory(String userId) throws Exception;

    void deleteOrderHistory(String userId, String tid) throws Exception;

    int deletePayFailedOrderHistoryItem(String userId, String tid) throws Exception;

    int deletePayFailedOrderAddress(String userId, String tid) throws Exception;

    int deletePayFailedOrderHistory(String userId, String tid) throws Exception;

    int countOrderHistoryByIdTid(String userId, String tid) throws Exception;

    int countUserOrderAddress(String userId, String tid) throws Exception;

    int countUserOrderHistoryItem(String userId, String tid) throws Exception;

    List<ProductItem> selectUserOrderHistoryItem(String userId, String tid) throws Exception;

    int deleteAllCurrentOrder() throws Exception;

    int deleteAllCurrentOrderItem() throws Exception;

    int deleteAllOrderHistory() throws Exception;

    int deleteAllOrderHistoryItem() throws Exception;

    int countOrderHistory() throws Exception;

    int countOrderHistoryItem() throws Exception;

    int countCurrentOrder() throws Exception;

    int countCurrentOrderItem() throws Exception;

    Long cancelOrder(String userId, String orderId, List<ProductItem> productItems) throws Exception;

    OrderHistoryDTO getOrderHistoryByUserIdAndOrderId(String userId, String orderId) throws Exception;

    int countUserOrderHistoryItemPaid(String userId, String tid) throws Exception;

    int updateUserOrderHistory(String orderStatus, Long totalPrice, String orderId, String userId) throws Exception;
}
