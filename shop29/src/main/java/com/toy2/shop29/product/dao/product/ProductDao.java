package com.toy2.shop29.product.dao.product;

import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.domain.product.ProductWithCategoriesDto;
import com.toy2.shop29.product.domain.product.ProductWithMiddleSmallDto;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    //1.write
    int insert(ProductDto product);

    //2-1.id로 product 1개만 read
    ProductDto select(int product_id);

    //2-2.전체 product를 read
    List<ProductDto> selectAll();

    //3.수정
    int update(ProductDto product);

    //4-1.삭제
    int delete(int product_id);

    //4-2.전체 삭제
    int deleteAll();

    // 총 product 갯수
    int count();

    //페이징
    List<ProductWithMiddleSmallDto> getPage(Map<String, Object> paramMap);

    //소분류Id에 맞는 상품 조회
    List<ProductDto> selectProductByCategory(int smallCategoryId);

    //특정 브랜드 상품 조회
    List<ProductDto> selectProductByBrand(int brandId);

    //특정 가격 범위 내의 상품 조회
    List<ProductDto> selectProductByPriceRange(int minPrice, int maxPrice);

    //정렬 관련
    //1.높은 가격순
    List<ProductWithMiddleSmallDto> sortedByPriceDesc(Map<String, Object> paramMap);

    //2.낮은 가격순
    List<ProductWithMiddleSmallDto> sortedByPriceAsc(Map<String, Object> paramMap);

    //3.신규순
    List<ProductWithMiddleSmallDto> sortedByNewest(Map<String, Object> paramMap);

    //4.할인율순
    List<ProductWithMiddleSmallDto> sortedByHighDiscount(Map<String, Object> paramMap);

    //5.별점 높은순
    List<ProductWithMiddleSmallDto> sortedByRating(Map<String, Object> paramMap);


    //제품ID로 상품의 분류 정보까지 조회
    ProductWithCategoriesDto selectProductWithCategories(int productId);



    int countMiddleCategory(int middleCategoryId);


    int getCountBySmallCategory(int smallCategoryId);

    int getCountByMiddleCategory(int middleCategoryId);

    Long checkProductStock(Long productId, Long optionValueId);

    int checkPurchaseAvailability(Long productId, Long optionValueId, Long quantity);
}
