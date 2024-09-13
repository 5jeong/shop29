package com.toy2.shop29.product.dao.product;

import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.domain.product.ProductWithCategoriesDto;
import com.toy2.shop29.product.domain.product.ProductWithMiddleSmallDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.ProductMapper.";



    @Override
    public ProductWithCategoriesDto selectProductWithCategories(int productId) {
        return session.selectOne(namespace + "selectProductWithCategories", productId);
    }

    @Override
    public int countProductBySmall(int smallCategoryId) {
        return session.selectOne(namespace + "countProductBySmall", smallCategoryId);
    }

    //중분류에 해당하는 상품 count
    @Override
    public int countProductByMiddle(int middleCategoryId) {
        return session.selectOne(namespace + "countProductByMiddle", middleCategoryId);
    }

    //정렬 관련
    //1.높은 가격순
    @Override
    public List<ProductWithMiddleSmallDto> sortByPriceDesc(Map<String, Object> paramMap) {
        return session.selectList(namespace + "sortByPriceDesc", paramMap);
    }

    //2.낮은 가격순
    @Override
    public List<ProductWithMiddleSmallDto> sortByPriceAsc(Map<String, Object> paramMap) {
        return session.selectList(namespace + "sortByPriceAsc", paramMap);
    }

    //3.신규순
    @Override
    public List<ProductWithMiddleSmallDto> sortByNewest(Map<String, Object> paramMap) {
        return session.selectList(namespace + "sortByNewest", paramMap);
    }

    //4.할인율순
    @Override
    public List<ProductWithMiddleSmallDto> sortByHighDiscount(Map<String, Object> paramMap) {
        return session.selectList(namespace + "sortByHighDiscount", paramMap);
    }

    //5.별점 높은순
    @Override
    public List<ProductWithMiddleSmallDto> sortByRating(Map<String, Object> paramMap) {
        return  session.selectList(namespace + "sortByRating", paramMap);
    }

    //페이징, 기본 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortDefault(Map<String, Object> paramMap) {
        return session.selectList(namespace + "sortDefault", paramMap);
    }

    @Override
    public Long checkProductStock(Long productId, Long optionValueId) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("optionValueId", optionValueId);
        return session.selectOne(namespace + "checkProductStock", params);
    }

    @Override
    public int updateProductStock(Long productId, Long optionValueId, Long quantity) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("optionValueId", optionValueId);
        params.put("quantity", quantity);
        return session.update(namespace + "updateProductStock", params);
    }

    @Override
    public Long getProductPriceByProductId(Long productId) {
        return session.selectOne(namespace + "getProductPriceByProductId", productId);
    }

    //2-1.id로 product 1개만 read
    @Override
    public ProductDto select(int product_id){
        return session.selectOne(namespace + "select",product_id); //?가 있다 싶으면 뒤에 Object parameter 사용해야됨
    }

    // 총 product 갯수
    @Override
    public int count() {
        return session.selectOne(namespace+"count");
    }

    @Override
    public int insert(ProductDto product){
        return session.insert(namespace + "insert",product); //map 사용하던가 아니면 product 사용하던가
    }
}










