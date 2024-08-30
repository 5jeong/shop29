package com.toy2.shop29.order.dao;

import com.toy2.shop29.common.ProductItem;
import com.toy2.shop29.order.domain.*;
import com.toy2.shop29.order.domain.response.OrderHistoryResponseDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private SqlSession session;

    private static String namespace = "com.toy2.shop29.order.dao.OrderDaoMapper.";

    @Override
    public int countOrderHistory() throws Exception {
        return session.selectOne(namespace + "countOrderHistory");
    }

    @Override
    public int countOrderHistoryItem() throws Exception {
        return session.selectOne(namespace + "countOrderHistoryItem");
    }

    @Override
    public List<CurrentOrderDTO> getCurrentOrderLists() throws Exception {
        return session.selectList(namespace + "getCurrentOrderLists");
    }

    @Override
    public int countCurrentOrder() throws Exception {
        return session.selectOne(namespace + "countCurrentOrder");
    }

    @Override
    public int countUserCurrentOrderById(String userId) throws Exception {
        return session.selectOne(namespace + "countUserCurrentOrderById", userId);
    }

    @Override
    public CurrentOrderDTO getCurrentOrderById(String userId) throws Exception {
        return session.selectOne(namespace + "getCurrentOrderById", userId);
    }

    @Override
    public int insertCurrentOrder(String userId) throws Exception {
        return session.insert(namespace + "insertCurrentOrder", userId);
    }

    @Override
    public int deleteCurrentOrder(String userId) throws Exception {
        return session.delete(namespace + "deleteCurrentOrder", userId);
    }

    @Override
    public List<OrderItemDTO> getUserCurrentOrderProducts(String userId) throws Exception {
        return session.selectList(namespace + "getUserCurrentOrderProducts", userId);
    }

    @Override
    public int insertCurrentOrderItem(String userId, Long productId, Long quantity, Long productOptionId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productId", productId);
        params.put("quantity", quantity);
        params.put("productOptionId", productOptionId);
        return session.insert(namespace + "insertCurrentOrderItem", params);
    }

    @Override
    public int updateUserCurrentOrderItemQuantity(String userId, Long productId, Long quantity) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productId", productId);
        params.put("quantity", quantity);
        return session.update(namespace + "updateUserCurrentOrderItemQuantity", params);
    }

    @Override
    public int deleteUserCurrentOrderItem(String userId, Long productId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productId", productId);
        return session.delete(namespace + "deleteUserCurrentOrderItem", params);
    }

    @Override
    public int deleteUserCurrentOrderAllItems(String userId) throws Exception {
        return session.delete(namespace + "deleteAllUserCurrentOrderItems", userId);
    }

    @Override
    public ShippingAddressInfoDTO selectShippingAddressInfoById(String userId) throws Exception {
        return session.selectOne(namespace + "selectShippingAddressInfoById", userId);
    }

    @Override
    public int insertOrderHistory(String orderId, String userId, String tid, Long totalPrice, Long shippingAddressId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("userId", userId);
        params.put("tid", tid);
        params.put("totalPrice", totalPrice);

        return session.insert(namespace + "insertOrderHistory", params);
    }

    @Override
    public int insertUserOrderHistoryItem(String orderId, String userId, Long productId, Long quantity, Long productOptionId, Long productPrice) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("userId", userId);
        params.put("productId", productId);
        params.put("quantity", quantity);
        params.put("productOptionId", productOptionId);
        params.put("productPrice", productPrice);
        return session.insert(namespace + "insertUserOrderHistoryItem", params);
    }

    @Override
    public Long getProductPrice(Long productId) {
        return session.selectOne(namespace + "getProductPrice", productId);
    }

    @Override
    public int insertOrderAddress(String orderId, String userId, ShippingAddressInfoDTO orderAddress) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("userId", userId);
        params.put("orderAddress", orderAddress);
        return session.insert(namespace + "insertOrderAddress", params);
    }

    @Override
    public String selectProductNameByProductId(Long productId){
        return session.selectOne(namespace + "selectProductNameByProductId", productId);
    }

    @Override
    public int updateUserOrderStatus(String userId, String tid, String orderStatus) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        params.put("orderStatus", orderStatus);
        return session.update(namespace + "updateUserOrderStatus", params);
    }

    @Override
    public List<OrderHistoryResponseDTO> getOrderHistoryInfoById(String orderId) throws Exception {
        return session.selectList(namespace + "getOrderHistoryInfoById", orderId);
    }

    @Override
    public int deleteAllCurrentOrder() throws Exception {
        return session.delete(namespace + "deleteAllCurrentOrder");
    }

    @Override
    public int countCurrentOrderItem() throws Exception {
        return session.selectOne(namespace + "countCurrentOrderItem");
    }

    @Override
    public int countUserCurrentOrderItemById(String userId) throws Exception {
        return session.selectOne(namespace + "countUserCurrentOrderItemById", userId);
    }

    @Override
    public int deleteAllCurrentOrderItem() throws Exception {
        return session.delete(namespace + "deleteAllCurrentOrderItem");
    }

    @Override
    public int deletePayFailedOrderHistory(String userId, String tid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        return session.update(namespace + "deletePayFailedOrderHistory", params);
    }

    @Override
    public int deletePayFailedOrderHistoryItem(String userId, String tid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        return session.update(namespace + "deletePayFailedOrderHistoryItem", params);
    }

    @Override
    public int deletePayFailedOrderAddress(String userId, String tid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        return session.update(namespace + "deletePayFailedOrderAddress", params);
    }

    @Override
    public int countOrderHistoryByIdTid(String userId, String tid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        return session.selectOne(namespace + "countOrderHistoryByIdTid", params);
    }

    @Override
    public int countUserOrderAddress(String userId, String tid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        return session.selectOne(namespace + "countUserOrderAddress", params);
    }

    @Override
    public int countUserOrderHistoryItem(String userId, String tid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        return session.selectOne(namespace + "countUserOrderHistoryItem", params);
    }

    @Override
    public int checkUserExists(String userId) throws Exception {
        return session.selectOne(namespace + "checkUserExists", userId);
    }

    @Override
    public int insertShippingAddress(String userId, ShippingAddressInfoDTO shippingAddressInfo) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("shippingAddressInfo", shippingAddressInfo);
        return session.insert(namespace + "insertShippingAddress", params);
    }

    @Override
    public int updateShippingAddress(String userId, ShippingAddressInfoDTO shippingAddressInfo) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("shippingAddressInfo", shippingAddressInfo);
        return session.update(namespace + "updateShippingAddress", params);
    }

    @Override
    public List<ProductItem> selectUserOrderHistoryItem(String userId, String tid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        return session.selectList(namespace + "selectUserOrderHistoryItem", params);
    }

    @Override
    public Long countProduct(Long productId) throws Exception {
        return session.selectOne(namespace + "countProduct", productId);
    }

    @Override
    public int deleteAllOrderHistory() throws Exception {
        return session.delete(namespace + "deleteAllOrderHistory");
    }

    @Override
    public int deleteAllOrderHistoryItem() throws Exception {
        return session.delete(namespace + "deleteAllOrderHistoryItem");
    }

    @Override
    public int updateUserOrderHistory(String orderStatus, Long totalPrice, String orderId, String userId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("orderStatus", orderStatus);
        params.put("totalPrice", totalPrice);
        params.put("orderId", orderId);
        params.put("userId", userId);
        return session.update(namespace + "updateUserOrderHistory", params);
    }

    @Override
    public OrderHistoryItemDto getOrderHistoryProduct(String userId, String orderId, Long productId, Long productOptionId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("orderId", orderId);
        params.put("productId", productId);
        params.put("productOptionId", productOptionId);
        return session.selectOne(namespace + "getOrderHistoryProduct", params);
    }

    @Override
    public int updateOrderHistoryItemStatus(String userId, String orderId, Long productId, Long productOptionId, String productOrderStatus) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("orderId", orderId);
        params.put("productId", productId);
        params.put("productOptionId", productOptionId);
        params.put("productOrderStatus", productOrderStatus);
        return session.update(namespace + "updateOrderHistoryItemStatus", params);
    }

    @Override
    public OrderHistoryDTO getOrderHistoryByUserIdAndOrderId(String userId, String orderId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("orderId", orderId);
        return session.selectOne(namespace + "getOrderHistoryByUserIdAndOrderId", params);
    }

    @Override
    public int countUserOrderHistoryItemPaid(String userId, String tid) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("tid", tid);
        return session.selectOne(namespace + "countUserOrderHistoryItemPaid", params);
    }
}
