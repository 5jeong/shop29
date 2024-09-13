package com.toy2.shop29.product.dao.product;

import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.domain.product.ProductWithCategoriesDto;
import com.toy2.shop29.product.domain.product.ProductWithMiddleSmallDto;

import java.util.List;
import java.util.Map;

public interface ProductDao {

    //제품ID로 상품의 분류 정보까지 조회
    ProductWithCategoriesDto selectProductWithCategories(int productId);

    int countProductBySmall(int smallCategoryId);

    int countProductByMiddle(int middleCategoryId);

    //정렬 관련
    //1.높은 가격순
    List<ProductWithMiddleSmallDto> sortByPriceDesc(Map<String, Object> paramMap);

    //2.낮은 가격순
    List<ProductWithMiddleSmallDto> sortByPriceAsc(Map<String, Object> paramMap);

    //3.신규순
    List<ProductWithMiddleSmallDto> sortByNewest(Map<String, Object> paramMap);

    //4.할인율순
    List<ProductWithMiddleSmallDto> sortByHighDiscount(Map<String, Object> paramMap);

    //5.별점 높은순
    List<ProductWithMiddleSmallDto> sortByRating(Map<String, Object> paramMap);

    //페이징
    List<ProductWithMiddleSmallDto> sortDefault(Map<String, Object> paramMap);

    Long checkProductStock(Long productId, Long optionValueId);

    int updateProductStock(Long productId, Long optionValueId, Long quantity);

    Long getProductPriceByProductId(Long productId);


    //2-1.id로 product 1개만 read
    ProductDto select(int product_id);

    int count();

    // write
    int insert(ProductDto product);
}
