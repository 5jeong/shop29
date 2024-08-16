package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.ProductOptionValueDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductOptionValueDao {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.toy2.shop29.product.dao.ProductOptionValueMapper.";

    public List<ProductOptionValueDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public ProductOptionValueDto selectById(int productOptionValueKey) {
        return session.selectOne(namespace + "selectById", productOptionValueKey);
    }

    public List<ProductOptionValueDto> selectByProductId(int productId) {
        return session.selectList(namespace + "selectByProductId", productId);
    }

    public int insert(ProductOptionValueDto productOptionValueDto) {
        return session.insert(namespace + "insert", productOptionValueDto);
    }

    public int update(ProductOptionValueDto productOptionValueDto) {
        return session.update(namespace + "update", productOptionValueDto);
    }

    public int delete(int productOptionValueKey) {
        return session.delete(namespace + "delete", productOptionValueKey);
    }
}
