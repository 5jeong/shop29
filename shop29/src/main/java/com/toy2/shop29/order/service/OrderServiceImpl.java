package com.toy2.shop29.order.service;

import com.toy2.shop29.cart.service.CartItemService;
import com.toy2.shop29.common.ProductItem;
import com.toy2.shop29.exception.error.ErrorCode;
import com.toy2.shop29.exception.error.ForbiddenAccessException;
import com.toy2.shop29.order.dao.OrderDao;
import com.toy2.shop29.order.domain.OrderHistoryDTO;
import com.toy2.shop29.order.domain.OrderHistoryItemDto;
import com.toy2.shop29.order.domain.OrderItemDTO;
import com.toy2.shop29.order.domain.ShippingAddressInfoDTO;
import com.toy2.shop29.order.domain.request.OrderCompletedRequestDTO;
import com.toy2.shop29.order.domain.request.OrderProductDto;
import com.toy2.shop29.order.domain.response.OrderHistoryResponseDTO;
import com.toy2.shop29.order.domain.response.OrderPageResponseDTO;
import com.toy2.shop29.order.exception.OrderException;
import com.toy2.shop29.order.utils.GenerateId;
import com.toy2.shop29.product.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final CartItemService cartItemService;
    private final ProductService productService;

    @Override
    public int updateUserOrderHistory(String orderStatus, Long totalPrice, String orderId, String userId) throws Exception {
        return orderDao.updateUserOrderHistory(orderStatus, totalPrice, orderId, userId);
    }

    @Override
    public int countUserOrderHistoryItemPaid(String userId, String tid) throws Exception {
        return orderDao.countUserOrderHistoryItemPaid(userId, tid);
    }

    @Override
    public OrderHistoryDTO getOrderHistoryByUserIdAndOrderId(String userId, String orderId) throws Exception {
        return orderDao.getOrderHistoryByUserIdAndOrderId(userId, orderId);
    }

    @Override
    public int countCurrentOrder() throws Exception {
        return orderDao.countCurrentOrder();
    }

    @Override
    public int countCurrentOrderItem() throws Exception {
        return orderDao.countCurrentOrderItem();
    }

    @Override
    public int countOrderHistory() throws Exception {
        return orderDao.countOrderHistory();
    }

    @Override
    public int countOrderHistoryItem() throws Exception {
        return orderDao.countOrderHistoryItem();
    }


    @Override
    public int deleteAllOrderHistory() throws Exception {
        return orderDao.deleteAllOrderHistory();
    }

    @Override
    public int deleteAllOrderHistoryItem() throws Exception {
        return orderDao.deleteAllOrderHistoryItem();
    }

    @Override
    public int deleteAllCurrentOrderItem() throws Exception {
        return orderDao.deleteAllCurrentOrderItem();
    }

    @Override
    public int deleteAllCurrentOrder() throws Exception {
        return orderDao.deleteAllCurrentOrder();
    }

    @Override
    public List<ProductItem> selectUserOrderHistoryItem(String userId, String tid) throws Exception {
        return orderDao.selectUserOrderHistoryItem(userId, tid);
    }

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
    public int addUserOrderHistoryItem(String orderId, String userId, Long productId, Long quantity, Long productOptionId, Long productPrice) throws Exception {
        return orderDao.insertUserOrderHistoryItem(orderId, userId, productId, quantity, productOptionId, productPrice);
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
    public List<OrderItemDTO> getCurrentOrderItems(String userId) throws Exception {
        return orderDao.getUserCurrentOrderProducts(userId);
    }

    @Override
    public int addProductToCurrentOrder(String userId, Long productId, Long quantity, Long productOptionId) throws Exception {
        return orderDao.insertCurrentOrderItem(userId, productId, quantity, productOptionId);
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
            Long productOptionId = product.getProductOptionId();
            Long stock = productService.checkProductStock(productId, productOptionId);
            if (stock - quantity < 0) {
                throw new OrderException(ErrorCode.OUT_OF_STOCK);
            }

            if (orderDao.countProduct(productId) < 1) {
                throw new ForbiddenAccessException();
            }
            int addResult = addProductToCurrentOrder(userId, productId, quantity, productOptionId);
            if (addResult > 1) {
                throw new ForbiddenAccessException();
            }
        }
        return 0;
    }

    // 주문 페이지 진입 시
    // 배송지, 상품 정보들 불러옴
    @Override
    @Transactional
    public OrderPageResponseDTO getCurrentOrderInfo(String userId) throws Exception {
        OrderPageResponseDTO orderResponse = new OrderPageResponseDTO();

        ShippingAddressInfoDTO address = orderDao.selectShippingAddressInfoById(userId);
        List<OrderItemDTO> orderItems = orderDao.getUserCurrentOrderProducts(userId);

        orderResponse.setShippingAddress(address);
        orderResponse.setCurrentItemsData(orderItems);

        return orderResponse;
    }


    // 결제 페이지 진입
    @Override
    @Transactional
    public int orderProcess(String userId, String tid, OrderCompletedRequestDTO orderRequest) throws Exception {

        String orderId = GenerateId.generateId();
        ShippingAddressInfoDTO shippingAddress = orderRequest.getShippingAddress();
        Long totalPrice = orderRequest.getTotalPrice();
        List<OrderProductDto> orderItems = orderRequest.getOrderItems();

        // 결제 페이지 진입 시
        // 주문 처리 테이블에 있던 컬럼은 삭제
        // 삭제 시 오류 발생하면 예외
        int deleteCurrentOrderProductsResult = deleteUserAllProducts(userId);
        int deleteCurrentOrderResult = deleteCurrentOrder(userId);
        int addOrderAddressResult = insertOrderAddress(orderId, userId, shippingAddress);

        if (deleteCurrentOrderResult != 1 || deleteCurrentOrderProductsResult < 1 || addOrderAddressResult != 1) {
            throw new ForbiddenAccessException();
        }

        int createOrderHistoryResult = createOrderHistory(orderId, userId, tid, totalPrice, shippingAddress.getShippingAddressId());

        if (createOrderHistoryResult != 1) {
            throw new OrderException(ErrorCode.CREATE_CART_ERROR);
        }

        for (OrderProductDto orderItem : orderItems) {
            Long productId = orderItem.getProductId();
            Long quantity = orderItem.getQuantity();
            Long productOptionId = orderItem.getProductOptionId();
            int updateProductStockResult = productService.checkPurchaseAvailability(productId, productOptionId, quantity);
            if (updateProductStockResult != 1) {
                throw new OrderException(ErrorCode.FORBIDDEN_ACCESS);
            }
            Long productPrice = productService.getProductPriceByProductId(productId);
            int addItems = addUserOrderHistoryItem(orderId, userId, productId, quantity, productOptionId, productPrice);
            if (addItems != 1) {
                throw new OrderException(ErrorCode.ADD_ITEM_ERROR);
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

    // 주문 내역 삭제
    @Override
    @Transactional
    public void deleteOrderHistory(String userId, String tid) throws Exception {
        List<ProductItem> orderHistoryItems = selectUserOrderHistoryItem(userId, tid);
        for (ProductItem orderItem : orderHistoryItems) {
            Long productId = orderItem.getProductId();
            Long quantity = orderItem.getQuantity();
            Long productOptionId = orderItem.getProductOptionId();
            int updateProductStockResult = productService.checkPurchaseAvailability(productId, productOptionId, -quantity);
            if (updateProductStockResult != 1) {
                throw new ForbiddenAccessException();
            }
        }

        int deleteAddressResult = deletePayFailedOrderAddress(userId, tid);
        int deleteOrderItem = deletePayFailedOrderHistoryItem(userId, tid);
        int deleteOrderHistory = deletePayFailedOrderHistory(userId, tid);
        if (deleteAddressResult != 1 || deleteOrderItem < 1 || deleteOrderHistory != 1) {
            throw new ForbiddenAccessException();
        }
    }

    // 주문내역 가져오기
    @Override
    @Transactional
    public List<OrderHistoryResponseDTO> getOrderHistory(String userId) throws Exception {
        return orderDao.getOrderHistoryInfoById(userId);
    }

    private void ensureUserCurrentOrderExists(String userId) throws Exception {
        if (checkUserCurrentOrderExist(userId) != 1) {
            createUserCurrentOrder(userId);
        }
    }

    @Override
    @Transactional
    public Long cancelOrder(String userId, String orderId, List<ProductItem> productItems) throws Exception {
        long totalRefundPrice = 0L;
        for (ProductItem productItem : productItems) {
            Long productId = productItem.getProductId();
            Long quantity = productItem.getQuantity();
            Long productOptionId = productItem.getProductOptionId();

            OrderHistoryItemDto orderHistoryItem = orderDao.getOrderHistoryProduct(userId, orderId, productId, productOptionId);
            orderDao.updateOrderHistoryItemStatus(userId, orderId, productId, productOptionId, "취소 완료");
            int updateProductInformation = productService.checkPurchaseAvailability(productId, productOptionId, -quantity);
            if (updateProductInformation != 1) {
                throw new ForbiddenAccessException();
            }
            totalRefundPrice += orderHistoryItem.getQuantity() * orderHistoryItem.getProductPrice();
        }
        return totalRefundPrice;
    }
}
