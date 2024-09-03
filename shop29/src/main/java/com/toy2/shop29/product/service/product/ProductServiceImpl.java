package com.toy2.shop29.product.service.product;

import com.toy2.shop29.product.dao.option.OptionDao;
import com.toy2.shop29.product.dao.product.ProductDao;
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



    @Override
    public ProductWithCategoriesDto findProductWithCategories(int productId) {
        return productDao.selectProductWithCategories(productId);
    }

    @Override
    public int countProductBySmall(int smallCategoryId) {
        return productDao.countProductBySmall(smallCategoryId);
    }

    @Override
    //특정 중분류에 해당하는 모든 제품 개수 세기
    public int countProductByMiddle(Integer middleCategoryId) {
        return productDao.countProductByMiddle(middleCategoryId);
    }

    //가격 오름차순으로 상품들 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByPriceDesc(Map<String, Object> paramMap) {
        return productDao.sortByPriceDesc(paramMap);
    }

    //가격 내림차순으로 상품들 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByPriceAsc(Map<String, Object> paramMap) {
        return productDao.sortByPriceAsc(paramMap);
    }

    //최신 상품순으로 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByNewest(Map<String, Object> paramMap) {
        return productDao.sortByNewest(paramMap);
    }

    //할인율 높은순으로 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByHighDiscount(Map<String, Object> paramMap) {
        return productDao.sortByHighDiscount(paramMap);
    }

    //별점 높은순으로 정렬
    @Override
    public List<ProductWithMiddleSmallDto> sortByRating(Map<String, Object> paramMap) {
        return productDao.sortByRating(paramMap);
    }

    @Override
    public List<ProductWithMiddleSmallDto> sortDefault(Map<String, Object> paramMap) {
        return productDao.sortDefault(paramMap);
    }

    // 상품 재고 확인
    @Override
    public Long checkProductStock(Long productId, Long optionValueId) {
        return productDao.checkProductStock(productId, optionValueId);
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

    //총 상품 개수
    @Override
    public int count() {
        return productDao.count();
    }

}