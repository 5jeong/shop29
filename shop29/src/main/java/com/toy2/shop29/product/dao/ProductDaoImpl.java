package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.ProductDto;
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

    //기본 CRUD 구현

    //1.write
    @Override
    public int insert(ProductDto product){
        return session.insert(namespace + "insert",product); //map 사용하던가 아니면 product 사용하던가
    }

    //2-1.id로 product 1개만 read
    @Override
    public ProductDto select(int product_id){
        return session.selectOne(namespace + "select",product_id); //?가 있다 싶으면 뒤에 Object parameter 사용해야됨
    }

    //2-2.전체 product를 read
    @Override
    public List<ProductDto> selectAll() {
        return session.selectList(namespace+"selectAll");
    }

    //3.수정
    @Override
    public int update(ProductDto product){
        return session.update(namespace + "update",product);
    }

    //4-1.삭제
    @Override
    public int delete(int product_id){
        return session.delete(namespace + "delete",product_id);
    }

    //4-2.전체 삭제
    @Override
    public int deleteAll() {
        return session.delete(namespace+"deleteAll");
    }

    // 총 product 갯수
    @Override
    public int count() {
        return session.selectOne(namespace+"count");
    }




    //페이징
    @Override
    public List<ProductDto> selectPage(Map map) {
        return session.selectList(namespace+"selectPage", map);
    }

//    public int increaseResultCnt(SearchCondition sc){
//        System.out.println("sc in searchResultCnt() = " + sc);
//        System.out.println("session =" + session);
//        return session.selectOne(namespace+"searchResultCnt",sc);
//    }
//
//    public List<ProductDto> searchSelectPage(SearchCondition sc){
//        return session.selectList(namespace+"searchSelectPage",sc);
//    }




    //특정 카테고리 상품 조회
    @Override
    public List<ProductDto> selectProductByCategory(int smallCategoryId){
        return session.selectList(namespace+"selectProductByCategory",smallCategoryId);
    }

    //특정 브랜드 상품 조회
    @Override
    public List<ProductDto> selectProductByBrand(int brandId){
        return session.selectList(namespace+"selectProductByBrand",brandId);
    }

    //특정 가격 범위 내의 상품 조회
    @Override
    public List<ProductDto> selectProductByPriceRange(int minPrice, int maxPrice){
        Map<String, Object> params = new HashMap<>();
        params.put("minPrice",minPrice);
        params.put("maxPrice",maxPrice);

        return session.selectList(namespace+"selectProductByPriceRange",params);
    }


    //정렬 관련
    //1.높은 가격순
    @Override
    public List<ProductDto> SortedByPriceDesc(Map<String, Object> paramMap){
        return session.selectList(namespace+"SortedByPriceDesc",paramMap);
    }

    //2.낮은 가격순
    @Override
    public List<ProductDto> SortedByPriceAsc(Map<String, Object> paramMap){
        return session.selectList(namespace+"SortedByPriceAsc",paramMap);
    }

    //3.신규순
    @Override
    public List<ProductDto> SortedByNew(Map<String, Object> paramMap){
        return session.selectList(namespace+"SortedByNew",paramMap);
    }

    //4.할인율순
    @Override
    public List<ProductDto> SortedByHighDiscount(Map<String, Object> paramMap){
        return session.selectList(namespace+"SortedByHighDiscount",paramMap);
    }

    //5.별점 높은순
    @Override
    public List<ProductDto> SortedByRating(Map<String, Object> paramMap){
        return session.selectList(namespace+"SortedByRating",paramMap);
    }





    @Override
    public int countMiddleCategory(int middleCategoryId) {
        return session.selectOne(namespace + "countMiddleCategory", middleCategoryId);
    }

    @Override
    public List<ProductDto> selectPageByMiddleCategory(Map<String, Object> map) {
        return session.selectList(namespace + "selectPageByCategory", map);
    }



}










