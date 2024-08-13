package com.toy2.shop29.product.service;

import com.toy2.shop29.product.dao.ProductDao;
import com.toy2.shop29.product.domain.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    //기본적인 CRUD

    //1.새 상품 등록 (insert)
    @Override
    public int addProduct(ProductDto productDto) {
        return productDao.insert(productDto);
    }

    //2-1.특정 product_id에 해당하는 상품 정보 조회하여 반환 (select)
    @Override
    public ProductDto read(Integer product_id){
        ProductDto productDto = productDao.select(product_id);
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
    @Override
    public List<ProductDto> getPage(Map map){
        return productDao.selectPage(map);
    }

    //3.상품 수정 (update)
    @Override
    public int modify(ProductDto productDto){
        return productDao.update(productDto);
    }

    //4. 주어진 product_id에 해당하는 상품 삭제 (delete)
    @Override
    public int remove(Integer product_id){
        return productDao.delete(product_id);
    }




    //총 상품 개수
    @Override
    public int getCount(){
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
    public List<ProductDto> sortByPriceDesc(Map<String, Object> paramap) {
        return productDao.SortedByPriceDesc(paramap);
    }

    //가격 오름차순으로 상품들 정렬
    @Override
    public List<ProductDto> sortByPriceAsc(Map<String, Object> paramap) {
        return productDao.SortedByPriceAsc(paramap);
    }

    //최신 상품순으로 정렬
    @Override
    public List<ProductDto> sortByNewest(Map<String, Object> paramap) {
        return productDao.SortedByNew(paramap);
    }

    //할인율 높은순으로 정렬
    @Override
    public List<ProductDto> sortByHighDiscount(Map<String, Object> paramap) {
        return productDao.SortedByHighDiscount(paramap);
    }

    //별점 높은순으로 정렬
    @Override
    public List<ProductDto> sortedByRating(Map<String, Object> paramap){
        return productDao.SortedByRating(paramap);
    };



//    @Override
//    //특정 중분류에 해당하는 모든 제품 개수 세기
//    public int getCountByMiddleCategory(Integer middleCategoryId) {
//        return productDao.countMiddleCategory(middleCategoryId);
//    }
//
//    @Override
//    //특정 중분류에 해당하는 모든 상품 페이징
//    public List<ProductDto> getPageByMiddleCategory(Map<String, Object> map) {
//        return productDao.selectPageByMiddleCategory(map);
//    }




//    public int getSearchResultCnt(SearchCondition sc){
//        return productDao.searchResultCnt(sc);
//    }
//
//    public List<ProductDto> getSearchResultPage(SearchCondition sc){
//        return productDao.searchSelectPage(sc);
//    }





}
