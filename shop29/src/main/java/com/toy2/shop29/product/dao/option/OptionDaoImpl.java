package com.toy2.shop29.product.dao.option;

import com.toy2.shop29.product.domain.option.ProductOptionDto;
import com.toy2.shop29.product.domain.option.ProductOptionValueDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OptionDaoImpl implements OptionDao {

    @Autowired
    private SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.OptionMapper.";


    //해당 상품이 가지고 있는 옵션을 가져오는 메서드
    @Override
    public List<ProductOptionDto> selectOptionKeys(int productId){
        return session.selectList(namespace+"selectOptionKeys", productId);
    }

    //해당 상품이 가지고 있는 옵션value를 가져오는 메서드
    @Override
    public List<ProductOptionValueDto> selectOptionValues(int productId){
        return session.selectList(namespace+"selectOptionValues", productId);
    }

}
