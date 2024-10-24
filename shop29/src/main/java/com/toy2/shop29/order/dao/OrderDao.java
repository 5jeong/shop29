package com.toy2.shop29.order.dao;

import com.toy2.shop29.common.ProductItem;
import com.toy2.shop29.order.domain.*;
import com.toy2.shop29.order.domain.response.OrderHistoryResponseDTO;

import java.util.List;

public interface OrderDao {
    /* --------주문 처리 --------*/
    // 주문 처리 모든 내역 조회
    List<CurrentOrderDTO> getCurrentOrderLists() throws Exception;

    // 주문 처리 개수 조회
    int countCurrentOrder() throws Exception;

    // 주문 처리 내역 유저별 조회
    int countUserCurrentOrderById(String userId) throws Exception;

    CurrentOrderDTO getCurrentOrderById(String userId) throws Exception;

    // 주문 처리 생성
    int insertCurrentOrder(String userId) throws Exception;

    int deleteCurrentOrder(String userId) throws Exception;

    List<OrderItemDTO> getUserCurrentOrderProducts(String userId) throws Exception;

    int insertCurrentOrderItem(String userId, Long productId, Long quantity, Long productOptionId) throws Exception;

    int updateUserCurrentOrderItemQuantity(String userId, Long productId, Long quantity) throws Exception;

    int deleteUserCurrentOrderItem(String userId, Long productId) throws Exception;

    int deleteUserCurrentOrderAllItems(String userId) throws Exception;

    ShippingAddressInfoDTO selectShippingAddressInfoById(String userId) throws Exception;

    int insertOrderHistory(String orderId, String userId, String tid, Long totalPrice, Long shippingAddressId) throws Exception;

    int insertUserOrderHistoryItem(String orderId, String userId, Long productId, Long quantity, Long productOptionId, Long productPrice) throws Exception;

    Long getProductPrice(Long productId);

    int insertOrderAddress(String orderId, String userId, ShippingAddressInfoDTO orderAddress) throws Exception;

    String selectProductNameByProductId(Long productId);

    int updateUserOrderStatus(String userId, String tid, String orderStatus) throws Exception;

    List<OrderHistoryResponseDTO> getOrderHistoryInfoById(String orderId) throws Exception;

    int deleteAllCurrentOrder() throws Exception;

    int countCurrentOrderItem() throws Exception;

    int countUserCurrentOrderItemById(String userId) throws Exception;

    int deleteAllCurrentOrderItem() throws Exception;

    int deletePayFailedOrderHistory(String userId, String tid) throws Exception;

    int deletePayFailedOrderHistoryItem(String userId, String tid) throws Exception;

    int deletePayFailedOrderAddress(String userId, String tid) throws Exception;

    int countOrderHistoryByIdTid(String userId, String tid) throws Exception;

    int countUserOrderAddress(String userId, String tid) throws Exception;

    int countUserOrderHistoryItem(String userId, String tid) throws Exception;

    int checkUserExists(String userId) throws Exception;

    int insertShippingAddress(String userId, ShippingAddressInfoDTO shippingAddressInfo) throws Exception;

    int updateShippingAddress(String userId, ShippingAddressInfoDTO shippingAddressInfo) throws Exception;

    List<ProductItem> selectUserOrderHistoryItem(String userId, String tid) throws Exception;

    Long countProduct(Long productId) throws Exception;

    int deleteAllOrderHistory() throws Exception;

    int deleteAllOrderHistoryItem() throws Exception;

    int countOrderHistory() throws Exception;

    int countOrderHistoryItem() throws Exception;

    int updateUserOrderHistory(String orderStatus, Long totalPrice, String orderId, String userId) throws Exception;

    OrderHistoryItemDto getOrderHistoryProduct(String userId, String orderId, Long productId, Long productOptionId) throws Exception;

    int updateOrderHistoryItemStatus(String userId, String orderId, Long productId, Long productOptionId, String productOrderStatus) throws Exception;

    OrderHistoryDTO getOrderHistoryByUserIdAndOrderId(String userId, String orderId) throws Exception;

    int countUserOrderHistoryItemPaid(String userId, String tid) throws Exception;
}
