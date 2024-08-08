//package com.toy2.shop29.product.service;
//
//import com.toy2.shop29.product.domain.ProductDto;
//import org.apache.ibatis.session.SqlSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ProductService {
//
//    @Autowired
//    SqlSession sqlSession;
//
//    String namespace = "com.toy2.shop29.dao.ProductMapper.";
//
//    //1. product_id 넣어서 맞는 product 찾기
//    public ProductDto SelectById(int product_id){
//
//    }
//
//    //2. product 수정하기
//    public int UpdateProduct(ProductDto product){
//
//    }
//
//    //3. product_id 넣어서 맞는 product 삭제하기
//    public int DeleteById(int product_id){
//
//    }
//
//    //4. product 수정하기
//    public int UpdateProduct(ProductDto){
//
//    }
//
//
//
//
//
//
//
//    //필터+검색(상품조회페이지 좌측에 있는 부분)
//    //5. product 가격, 색상, 브랜드, 무료배송 상품인지, 품절상품 인지, 할인상품인지 넣어서 그에 맞는 product 서치
//    public ProductDto SearchProduct(int pice, int color_id, int brand_id, 품절상품, 할인중, 임시품절){
//
//    }
//    //search condition 너무많으면 묶기 << 배열이나
//
//    //6. 모든 상품 페이징(ALL)
//
//    //7. 카테고리별로 페이징
//
//    //8. NEW 상품 페이징
//
//    //9. EXCLUSIVE 상품 페이징
//
//
//
//
//
//
//
//
//
//
//    //위의 search필터는 인자들을 복합적으로 입력받는 것에 비해, 정렬은 단일 인자 기준으로 나오다 보니 하나하나 다 만들어 줘야되나??
//    //10. 추천순으로 정렬
//    public List<ProductDto> SortingByRecommend(int ProductRecommend){
//
//    }
//
//    //11. 가격 높은순으로 정렬
//    public List<ProductDto> SortingByDescending(int price){
//
//    }
//
//
//    //12. 가격 낮은순으로 정렬
//    public List<ProductDto> SortingByAscending(int price){
//
//    }
//
//
//    //13. 신상품 순으로 정렬
//    public List<ProductDto> SortingByNew(int created_date){
//
//    }
//
//    //14. 리뷰 많은 순으로 정렬
//    public List<ProductDto> SortingByReviewCnt(int product_review_cnt){
//
//    }
//
//    //15. 높은 할인순으로 정렬
//    public List<ProductDto> SortingBySaleRatio(int sale_ratio){
//
//    }
//
//    //16. 좋아요 많은순으로 정렬
//    public List<ProductDto> SortingByLike(int product_like_cnt){
//
//    }
//
//    //17. 판매량 순으로 정렬
//    //결제가 성공적으로 이루어진 주문의 갯수를 가져와야 할 듯함
//    public List<ProductDto> SortingBySaleCnt(int product_sale_cnt){
//
//    }
//
//
//
//
//
//
//
//    //18. 제품이 받은 총 좋아요 카운트
//
//    //19. 제품이 받은 별점 평균
//
//    //20. 제품이 받은 총 리뷰 카운터
//
//
//
//
//
//
//
//    //21. 제품 구매나 장바구니 넣을때 제품옵션을 선택 했는지 안했는지 체크
//
//
//
//
//
//
//    //22. 제품 썸네일 이미지 변경
//
//
//
//
//
//
//
//
//
//
//}
