package com.toy2.shop29.product.service.product;

import com.toy2.shop29.product.dao.option.OptionDao;
import com.toy2.shop29.product.dao.product.ProductDao;
import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.domain.product.ProductWithCategoriesDto;
import com.toy2.shop29.product.domain.product.ProductWithMiddleSmallDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    private OptionDao optionDao;

    //기본적인 CRUD

    //1.새 상품 등록 (insert)
    @Override
    public int addProduct(ProductDto productDto) {
        return productDao.insert(productDto);
    }

    //2-1.특정 product_id에 해당하는 상품 정보 조회하여 반환 (select)
    @Override
    public ProductDto read(Integer productId) {
        ProductDto productDto = productDao.select(productId);
        //원래는 조회수+1 시키는게 여기에 있는데 나는 따로 조회수 신경 안써서 패스
        return productDto;
    }

    //2-2.모든 상품 정보 조회해서 반환 (select)
    @Override
    public List<ProductDto> getList() {
        return productDao.selectAll();
    }

    //2-3.특정 페이지에 해당하는 상품리스트 조회해서 반환 (select)
    //offest과 pageSize 필요해서 map을 인자로 받는다

    //3.상품 수정 (update)
    @Override
    public int modify(ProductDto productDto) {
        return productDao.update(productDto);
    }

    //4. 주어진 product_id에 해당하는 상품 삭제 (delete)
    @Override
    public int remove(Integer product_id) {
        return productDao.delete(product_id);
    }

    // 상품 재고 수정
    @Override
    public int checkPurchaseAvailability(Long productId, Long optionValueId, Long quantity) {
        long stock = checkProductStock(productId, optionValueId) - quantity;
        if (stock < 0) {
            return 0;
        }
        return productDao.updateProductStock(productId, optionValueId, stock);
    }
  
    // 상품 재고 확인
    @Override
    public Long checkProductStock(Long productId, Long optionValueId) {
        return productDao.checkProductStock(productId, optionValueId);
    }

    //총 상품 개수
    @Override
    public int getCount() {
        return productDao.count();
    }

    //특정 카테고리에 해당하는 상품들 조회
    @Override
    public List<ProductDto> getProductsByCategory(int categoryId) {
        return productDao.selectProductByCategory(categoryId);
    }

    //특정 브랜드에 해당하는 상품들을 조회
    @Override
    public List<ProductDto> getProductsByBrand(int brandId) {
        return productDao.selectProductByBrand(brandId);
    }

    //특정 가격 범위 안에 있는 상품들을 조회
    @Override
    public List<ProductDto> getProductsByPriceRange(int minPrice, int maxPrice) {
        return productDao.selectProductByPriceRange(minPrice, maxPrice);
    }

    //가격 내림차순으로 상품들 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByPriceAsc(Map<String, Object> paramMap) {
        return productDao.sortedByPriceAsc(paramMap);
    }

    //가격 오름차순으로 상품들 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByPriceDesc(Map<String, Object> paramMap) {
        return productDao.sortedByPriceDesc(paramMap);
    }
    //최신 상품순으로 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByNewest(Map<String, Object> paramMap) {
        return productDao.sortedByNewest(paramMap);
    }

    //할인율 높은순으로 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByHighDiscount(Map<String, Object> paramMap) {
        return productDao.sortedByHighDiscount(paramMap);
    }

    //별점 높은순으로 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortedByRating(Map<String, Object> paramMap) {
        return productDao.sortedByRating(paramMap);
    }

    @Override
    public List<ProductWithMiddleSmallDto> getPage(Map<String, Object> paramMap) {
        return productDao.getPage(paramMap);
    }


    @Override
    public ProductWithCategoriesDto getProductWithCategories(int productId) {
        return productDao.selectProductWithCategories(productId);
    }


    @Override
    //특정 중분류에 해당하는 모든 제품 개수 세기
    public int getCountByMiddleCategory(Integer middleCategoryId) {
        return productDao.countMiddleCategory(middleCategoryId);
    }


    @Override
    public int getCountBySmallCategory(int smallCategoryId) {
        return productDao.getCountBySmallCategory(smallCategoryId);
    }

    @Override
    public int getCountByMiddleCategory(int middleCategoryId) {
        return productDao.getCountByMiddleCategory(middleCategoryId);
    }





//    public int getSearchResultCnt(SearchCondition sc){
//        return productDao.searchResultCnt(sc);
//    }
//
//    public List<ProductDto> getSearchResultPage(SearchCondition sc){
//        return productDao.searchSelectPage(sc);
//    }





}
