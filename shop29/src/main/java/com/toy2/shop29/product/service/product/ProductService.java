package com.toy2.shop29.product.service.product;

import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.domain.product.ProductWithCategoriesDto;
import com.toy2.shop29.product.domain.product.ProductWithMiddleSmallDto;

import java.util.List;
import java.util.Map;

public interface ProductService {
    //1.새 상품 등록 (insert)
    int addProduct(ProductDto productDto);

    //2-1.특정 product_id에 해당하는 상품 정보 조회하여 반환 (select)
    ProductDto read(Integer producId);

    //2-2.모든 상품 정보 조회해서 반환 (select)
    List<ProductDto> getList();

    //3.상품 수정 (update)
    int modify(ProductDto productDto);

    //4. 주어진 product_id에 해당하는 상품 삭제 (delete)
    int remove(Integer productId);

    //총 상품 개수
    int getCount();

    //특정 카테고리에 해당하는 상품들 조회
    List<ProductDto> getProductsByCategory(int categoryId);

    //특정 브랜드에 해당하는 상품들을 조회
    List<ProductDto> getProductsByBrand(int brandId);

    //특정 가격 범위 안에 있는 상품들을 조회
    List<ProductDto> getProductsByPriceRange(int minPrice, int maxPrice);




    //가격 내림차순으로 상품들 정렬
    List<ProductWithMiddleSmallDto> sortByPriceAsc(Map<String, Object> paramMap);

    //가격 오름차순으로 상품들 정렬
    List<ProductWithMiddleSmallDto> sortByPriceDesc(Map<String, Object> paramMap);

    //최신 상품순으로 정렬
    List<ProductWithMiddleSmallDto> sortByNewest(Map<String, Object> paramMap);

    //할인율 높은순으로 정렬
    List<ProductWithMiddleSmallDto> sortByHighDiscount(Map<String, Object> paramMap);

    //높은 별점순으로 정렬
    List<ProductWithMiddleSmallDto> sortedByRating(Map<String, Object> paramMap);

    List<ProductWithMiddleSmallDto> getPage(Map<String, Object> paramMap);



    //상품에 해당하는 모든 카테고리 가져오기
    ProductWithCategoriesDto getProductWithCategories(int productId);

    //특정 중분류에 해당하는 모든 제품 개수 세기
    int getCountByMiddleCategory(Integer middleCategoryId);



    int getCountBySmallCategory(int smallCategoryId);

    int getCountByMiddleCategory(int middleCategoryId);

    Long checkProductStock(Long productId, Long optionValueId);

    int checkPurchaseAvailability(Long productId, Long optionValueId, Long quantity);
}
