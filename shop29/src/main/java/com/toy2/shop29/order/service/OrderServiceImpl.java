package com.toy2.shop29.order.service;

import com.toy2.shop29.order.dao.OrderDao;
import com.toy2.shop29.order.domain.OrderItemDTO;
import com.toy2.shop29.order.domain.ShippingAddressInfoDTO;
import com.toy2.shop29.order.domain.request.OrderProductDto;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;
import com.toy2.shop29.order.domain.response.OrderHistoryDTO;
import com.toy2.shop29.order.domain.response.OrderPageResponseDTO;
import com.toy2.shop29.order.utils.GenerateId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public int countUserOrderHistoryItem(String userId, String tid) throws Exception {
        return orderDao.countUserOrderHistoryItem(userId, tid);
    }

    @Override
    public int countUserOrderAddress(String userId, String tid) throws Exception {
        return orderDao.countUserOrderAddress(userId, tid);
    }

    @Override
    public int countOrderHistoryByIdTid(String userId, String tid) throws Exception {
        return orderDao.countOrderHistoryByIdTid(userId, tid);
    }

    @Override
    public int deletePayFailedOrderHistory(String userId, String tid) throws Exception {
        return orderDao.deletePayFailedOrderHistory(userId, tid);
    }

    @Override
    public int deletePayFailedOrderAddress(String userId, String tid) throws Exception {
        return orderDao.deletePayFailedOrderAddress(userId, tid);
    }

    @Override
    public int deletePayFailedOrderHistoryItem(String userId, String tid) throws Exception {
        return orderDao.deletePayFailedOrderHistoryItem(userId, tid);
    }

    @Override
    public int updateUserOrderStatus(String userId, String tid, String orderStatus) throws Exception {
        return orderDao.updateUserOrderStatus(userId, tid, orderStatus);
    }

    @Override
    public String selectProductNameByProductId(Long productId) {
        return orderDao.selectProductNameByProductId(productId);
    }

    @Override
    public int insertOrderAddress(String orderId, String userId, ShippingAddressInfoDTO orderAddress) throws Exception {
        return orderDao.insertOrderAddress(orderId, userId, orderAddress);
    }

    @Override
    public int addUserOrderHistoryItem(String orderId, String userId, Long productId, Long quantity) throws Exception {
        return orderDao.insertUserOrderHistoryItem(orderId, userId, productId, quantity);
    }

    @Override
    public Long getProductPrice(Long productId) {
        return orderDao.getProductPrice(productId);
    }

    @Override
    public int createOrderHistory(String orderId, String userId, String tid, Long totalPrice, Long shippingAddressId) throws Exception {
        return orderDao.insertOrderHistory(orderId, userId, tid, totalPrice, shippingAddressId);
    }

    @Override
    public int deleteCurrentOrder(String userId) throws Exception {
        return orderDao.deleteCurrentOrder(userId);
    }

    @Override
    public int checkUserCurrentOrderExist(String userId) throws Exception {
        return orderDao.countUserCurrentOrderById(userId);
    }

    @Override
    public int createUserCurrentOrder(String userId) throws Exception {
        return orderDao.insertCurrentOrder(userId);
    }

    @Override
    public List<OrderItemDTO> getOrderItems(String userId) throws Exception {
        return orderDao.getUserCurrentOrderProducts(userId);
    }

    @Override
    public int addProductToCurrentOrder(String userId, Long productId, Long quantity) throws Exception {
        return orderDao.insertCurrentOrderItem(userId, productId, quantity);
    }

    @Override
    public int updateProductsToCurrentOrder(String userId, Long productId, Long quantity) throws Exception {
        return orderDao.updateUserCurrentOrderItemQuantity(userId, productId, quantity);
    }

    @Override
    public int deleteUserAllProducts(String userId) throws Exception {
        return orderDao.deleteUserCurrentOrderAllItems(userId);
    }

    @Override
    @Transactional
    public int addProducts(String userId, List<OrderProductDto> products) throws Exception {
        ensureUserCurrentOrderExists(userId);
        deleteUserAllProducts(userId);
        for (OrderProductDto product : products) {
            Long productId = product.getProductId();
            Long quantity = product.getQuantity();
            int addResult = addProductToCurrentOrder(userId, productId, quantity);
            if (addResult > 1) {
                throw new IllegalArgumentException("비정상적인 접근입니다.");
            }
        }
        return 0;
    }

    @Override
    public OrderPageResponseDTO getCurrentOrderInfo(String userId) throws Exception {
        OrderPageResponseDTO orderResponse = new OrderPageResponseDTO();

        ShippingAddressInfoDTO address = orderDao.selectShippingAddressInfoById(userId);
        List<OrderItemDTO> orderItems = orderDao.getUserCurrentOrderProducts(userId);

        orderResponse.setShippingAddress(address);
        orderResponse.setCurrentItemsData(orderItems);

        return orderResponse;
    }


    @Override
    @Transactional
    public int orderProcess(String userId, String tid, OrderCompletedRequestDTO orderRequest) throws Exception {
        String orderId = GenerateId.generateId();
        ShippingAddressInfoDTO shippingAddress = orderRequest.getShippingAddress();
        Long totalPrice = orderRequest.getTotalPrice();
        List<OrderProductDto> orderItems = orderRequest.getOrderItems();

        int deleteCurrentOrderProductsResult = deleteUserAllProducts(userId);
        int deleteCurrentOrderResult = deleteCurrentOrder(userId);
        int addOrderAddressResult = insertOrderAddress(orderId, userId, shippingAddress);

        if (deleteCurrentOrderResult != 1 || deleteCurrentOrderProductsResult < 1 || addOrderAddressResult != 1) {
            throw new IllegalArgumentException("비정상적인 접근입니다.");
        }

        int createCurrentOrderResult = createOrderHistory(orderId, userId, tid, totalPrice, shippingAddress.getShippingAddressId());

        if (createCurrentOrderResult != 1) {
            throw new IllegalArgumentException("서버 오류");
        }

        for (OrderProductDto orderItem : orderItems) {
            Long productId = orderItem.getProductId();
            Long quantity = orderItem.getQuantity();
            int addItems = addUserOrderHistoryItem(orderId, userId, productId, quantity);
            if (addItems != 1) {
                throw new IllegalArgumentException("서버 오류");
            }
        }
        int userExists = orderDao.checkUserExists(userId);
        int isDefault = shippingAddress.getIsDefault();

        if (isDefault == 1) {
            // 유저가 존재하면 업데이트
            if (userExists > 0) {
                orderDao.updateShippingAddress(userId, shippingAddress);
            } else {
                // 유저가 존재하지 않으면 삽입
                orderDao.insertShippingAddress(userId, shippingAddress);
            }
        }
        return 1;
    }

    @Override
    @Transactional
    public void deleteOrderHistory(String userId, String tid) throws Exception {

        int deleteAddressResult = deletePayFailedOrderAddress(userId, tid);
        int deleteOrderItem = deletePayFailedOrderHistoryItem(userId, tid);
        int deleteOrderHistory = deletePayFailedOrderHistory(userId, tid);

        if (deleteAddressResult != 1 || deleteOrderItem < 1 || deleteOrderHistory != 1) {
            throw new IllegalArgumentException("비정상적 접근입니다.");

        }
    }

    @Override
    @Transactional
    public List<OrderHistoryDTO> getOrderHistory(String userId) throws Exception {
        return orderDao.getOrderHistoryInfoById(userId);
    }

    private void ensureUserCurrentOrderExists(String userId) throws Exception {
        if (checkUserCurrentOrderExist(userId) != 1) {
            createUserCurrentOrder(userId);
        }
    }
}
