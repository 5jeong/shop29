package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.ProductDto;

import java.util.List;

public interface ProductDao {
    //read
    ProductDto select(int product_id);

    //전체 read
    List<ProductDto> selectAll();

    //write
    int insert(ProductDto product);

    //수정
    int update(ProductDto product);

    //특정 product_id에 맞는 product 하나 삭제
    int delete(int product_id);

    //브랜드 나가서 해당 브랜드의 상품 모두 delete
    int deleteByBrand(String brand_id);

    //전체 삭제
    int deleteAll();

    // 총 product 갯수
    int count();

    //같은 brand_id를 가진 제품의 갯수
    int brandCount(String brand_id);



}
