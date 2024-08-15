package com.toy2.shop29.product.controller;

import com.toy2.shop29.product.domain.PageHandler;
import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.domain.ProductWithCategoriesDto;
import com.toy2.shop29.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;



    // 상품 상세 페이지 이동
    @GetMapping("/{productId}")
    public String getProductDetail(@PathVariable("productId") int productId, Model model) {

        // 상품과 관련된 카테고리 정보를 포함한 상세 정보를 가져옵니다
        ProductWithCategoriesDto product = productService.getProductWithCategories(productId);

        //error/404페이지 아직 안 만들어서 주석 처리
//        if (product == null) {
//            // 상품이 존재하지 않는 경우 에러 페이지로 이동
//            return "error/404";
//        }

        // 모델에 해당 상품 정보를 추가하여 뷰에 전달
        model.addAttribute("product", product);

        // 상세 페이지로 이동
        return "product/detail";
    }


    //상품 게시판으로 이동
    @GetMapping("/list")
    //page와 pageSize로 offset을 계산하기 때문에 필요하고, 값을 넘겨주기 위해 model이 필요
    public String list(@RequestParam(name = "page", required = false) Integer page,
                       @RequestParam(name = "pageSize", required = false) Integer pageSize,
                       @RequestParam(name = "sortOption", required = false, defaultValue = "") String sortOption,  // 정렬 옵션
                       Model model){

        //레퍼런스페이지 기준 pageSize 50
        if(page==null){ page=1; }
        if(pageSize==null){ pageSize=50; }



        try{
            //전체 상품 개수 가져오기
            int totalCnt = productService.getCount();

            //pageHandler에 계산된 totalCnt와 param으로 가져온 page,Pasize보내기
            PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);

            //sort된 상품 리스트를 담을 list
            List<ProductDto> list;

            //페이징을 할때 사용할 offset과 pageSize 정보를 map에 저장
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("offset", ((page-1)*pageSize));
            paramMap.put("pageSize", pageSize);


            // param으로 받은 정렬옵션에 따른 상품 목록(list)을 switch로 조절
            //switch문을 service단으로 옮기기
//            list = productService.sort(sortOption,paramMap); <<
            switch (sortOption) {
                case "priceDesc": //높은 가격 순
                    list = productService.sortByPriceDesc(paramMap);
                    break;
                case "priceAsc": //낮은 가격순
                    list = productService.sortByPriceAsc(paramMap);
                    break;
                case "newest": //최신순
                    list = productService.sortByNewest(paramMap);
                    break;
                case "highDiscount": //높은 할인율순
                    list = productService.sortByHighDiscount(paramMap);
                    break;
                case "rating": //높은 별점순
                    list = productService.sortedByRating(paramMap);
                    break;
                default: //기본 정렬
                    list = productService.getPage(paramMap);
                    break;
            }



            model.addAttribute("list",list);
            model.addAttribute("pageHandler",pageHandler);
            model.addAttribute("sortOption", sortOption);




        }catch (Exception e){
            e.printStackTrace();
//            return "error/500";
        }


        return "product/productList";

    }


}
