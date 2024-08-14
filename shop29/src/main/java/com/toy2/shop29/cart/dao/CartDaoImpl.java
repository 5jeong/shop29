package com.toy2.shop29.cart.dao;

import com.toy2.shop29.cart.domain.response.CartDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CartDaoImpl implements CartDao {
    @Autowired
    private SqlSession session;

    private static String namespace = "com.toy2.shop29.cart.dao.CartDaoMapper.";

    @Override
    public int countCart() throws Exception {
        return session.selectOne(namespace + "countCart");
    }

    @Override
    public int countUserCartProducts(String userId) throws Exception {
        return session.selectOne(namespace + "countUserCartProducts", userId);
    }

    @Override
    public int countUserCart(String userId) {
        return session.selectOne(namespace + "countUserCart", userId);
    }

    @Override
    public int createCart(String userId, int is_user) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("isUser", is_user);
        return session.insert(namespace + "createCart", params);
    }
    
    @Override
    public List<CartDto> selectUserCartProductsByUserId(String userId) throws Exception {
        return session.selectList(namespace + "selectUserCartProductsByUserId", userId);
    }

    @Override
    public CartDto searchProductIdByUserIdAndProductId(String userId, Long productId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("productId", productId);

        return session.selectOne(namespace + "searchProductIdByUserIdAndProductId", map);
    }

    @Override
    public int insertUserCartProduct(String userId, Long productId, Long quantity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("productId", productId);
        map.put("quantity", quantity);

        return session.insert(namespace + "insertUserCartProduct", map);
    }

    @Override
    public int updateCartLastUpdate(String userId) throws Exception {
        return session.update(namespace + "updateCartLastUpdate", userId);
    }

    @Override
    public int updateUserCartProductQuantity(String userId, Long productId, Long quantity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("productId", productId);
        map.put("quantity", quantity);

        return session.update(namespace + "updateUserCartProductQuantity", map);
    }

    @Override
    public int deleteUserCartProduct(String userId, Long productId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("productId", productId);
        return session.delete(namespace + "deleteUserCartProduct", map);
    }

    @Override
    public int deleteUserCart(String userId) {
        return session.delete(namespace + "deleteUserCart", userId);
    }

    @Override
    public int deleteCart() throws Exception {
        return session.delete(namespace + "deleteCart");
    }

    @Override
    public int deleteCartItem() throws Exception {
        return session.delete(namespace + "deleteCartItem");
    }

    @Override
    public int countCartItem() throws Exception {
        return session.selectOne(namespace + "countCartItem");
    }
}
