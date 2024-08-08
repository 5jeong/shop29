package com.toy2.shop29.cart.dao;

import com.toy2.shop29.cart.domain.CartDto;
import org.apache.ibatis.annotations.Param;
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
    public List<CartDto> findcartsByUserId(String userId) {
        return session.selectList(namespace + "findcartsByUserId", userId);
    }

    @Override
    public CartDto searchProductIdByUserIdAndProductId(String userId, String productId) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("productId", productId);

        return session.selectOne(namespace + "searchProductIdByUserIdAndProductId", map);
    }

    @Override
    public int insertCart(String userId, String productId) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("productId", productId);
        return session.insert(namespace + "insertCart", map);
    }

    @Override
    public int updatecartQuantity(String userId, String productId, Integer quantity) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("productId", productId);
        map.put("quantity", quantity);

        return session.update(namespace + "updateCart", map);
    }

    @Override
    public int deleteCart(String userId, String productId) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("productId", productId);
        return session.delete(namespace + "deleteCart", map);
    }
}
