package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.ProductOptionDto;
import com.toy2.shop29.product.domain.ProductStockDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductOptionDao {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.toy2.shop29.product.dao.ProductOptionMapper.";

    public List<ProductOptionDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public ProductOptionDto selectById(int productOptionId) {
        return session.selectOne(namespace + "selectById", productOptionId);
    }

    public List<ProductOptionDto> selectByProductId(int productId) {
        return session.selectList(namespace + "selectByProductId", productId);
    }

    public int insert(ProductOptionDto productOptionDto) {
        return session.insert(namespace + "insert", productOptionDto);
    }

    public int update(ProductOptionDto productOptionDto) {
        return session.update(namespace + "update", productOptionDto);
    }

    public int delete(int productOptionId) {
        return session.delete(namespace + "delete", productOptionId);
    }

    public List<ProductStockDto> selectProductStockByOptions(Map<String, Object> params){
        return session.selectList(namespace + "selectProductStockByOptions", params);
    }







}
