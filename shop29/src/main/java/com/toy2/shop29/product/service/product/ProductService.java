package com.toy2.shop29.product.service.product;

import com.toy2.shop29.product.domain.product.ProductWithCategoriesDto;
import com.toy2.shop29.product.domain.product.ProductWithMiddleSmallDto;

import java.util.List;
import java.util.Map;

public interface ProductService {


    //상품에 해당하는 모든 카테고리 가져오기
    ProductWithCategoriesDto findProductWithCategories(int productId);

    int countProductBySmall(int smallCategoryId);

    //특정 중분류에 해당하는 모든 제품 개수 세기
    int countProductByMiddle(Integer middleCategoryId);

    //가격 오름차순으로 상품들 정렬
    List<ProductWithMiddleSmallDto> sortByPriceDesc(Map<String, Object> paramMap);

    //가격 내림차순으로 상품들 정렬
    List<ProductWithMiddleSmallDto> sortByPriceAsc(Map<String, Object> paramMap);

    //최신 상품순으로 정렬
    List<ProductWithMiddleSmallDto> sortByNewest(Map<String, Object> paramMap);

    //할인율 높은순으로 정렬
    List<ProductWithMiddleSmallDto> sortByHighDiscount(Map<String, Object> paramMap);

    //높은 별점순으로 정렬
    List<ProductWithMiddleSmallDto> sortByRating(Map<String, Object> paramMap);

    List<ProductWithMiddleSmallDto> sortDefault(Map<String, Object> paramMap);

    Long checkProductStock(Long productId, Long optionValueId);

    int checkPurchaseAvailability(Long productId, Long optionValueId, Long quantity);

    //총 상품 개수
    int count();

}