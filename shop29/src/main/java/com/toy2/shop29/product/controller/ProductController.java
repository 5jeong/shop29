package com.toy2.shop29.product.controller;

import com.toy2.shop29.product.domain.PageHandler;
import com.toy2.shop29.product.domain.category.MiddleCategoryDto;
import com.toy2.shop29.product.domain.category.SmallCategoryDto;
import com.toy2.shop29.product.domain.option.ProductOptionDto;
import com.toy2.shop29.product.domain.option.ProductOptionValueDto;
import com.toy2.shop29.product.domain.product.ProductWithCategoriesDto;
import com.toy2.shop29.product.domain.product.ProductWithMiddleSmallDto;
import com.toy2.shop29.product.service.category.CategoryService;
import com.toy2.shop29.product.service.option.OptionService;
import com.toy2.shop29.product.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OptionService optionService;

    // 상품 상세 페이지 이동
    @GetMapping("/product/{productId}")
    public String getProductDetail(@PathVariable("productId") int productId, Model model) {
        // 상품과 관련된 카테고리 정보를 포함한 상세 정보를 가져옵니다
        try {
            ProductWithCategoriesDto product = productService.findProductWithCategories(productId);
            // 모델에 해당 상품 정보를 추가하여 뷰에 전달
            model.addAttribute("product", product);
            // 상세 페이지로 이동

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        // 옵션 관련 로직
        List<ProductOptionDto> productOptions = optionService.findProductOptions(productId);
        List<ProductOptionValueDto> productOptionValues = optionService.findProductOptionValues(productId);
        model.addAttribute("productOptions", productOptions);
        model.addAttribute("productOptionValues", productOptionValues);


        ProductWithCategoriesDto product = productService.findProductWithCategories(productId);
        model.addAttribute("product", product);

        return "product/detail";
    }


    //상품 게시판으로 이동
    @GetMapping("/product/list")
    //page와 pageSize로 offset을 계산하기 때문에 필요하고, 값을 넘겨주기 위해 model이 필요
    public String list(@RequestParam(name = "page", required = false) Integer page,
                       @RequestParam(name = "pageSize", required = false) Integer pageSize,
                       @RequestParam(name = "sortOption", required = false, defaultValue = "") String sortOption,  // 정렬 옵션
                       @RequestParam(name = "middleCategoryId", required = false) Integer middleCategoryId,
                       @RequestParam(name = "smallCategoryId", required = false) Integer smallCategoryId,
                       Model model) {

        //레퍼런스페이지 기준 pageSize 50
        //RequestParam의 defaultValue는 String형만 가능해서 따로 빼놓음
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 50;
        }

        //메인페이지를 타고 오는게 아닌 url로 /product/list 직접 치고 들어오는거라면 middleCategoryId=1인 상태로
        if (middleCategoryId == null) {
            middleCategoryId = 1;
        }


        // smallCategoryId가 null일때 : home에서 들어왔을때, 다른 중분류 목록의 이름을 눌렀을때
        // smallCategoryId가 null아닐때: 중분류에 대한 소분류 목록의 이름을 눌렀을때
        if (smallCategoryId != null) {
            MiddleCategoryDto middleCategoryDto = categoryService.findMiddleBySmall(smallCategoryId);
            //소분류 이름을 눌러 middleCategoryId가 null이어도 소분류Id로 중분류Id를 뽑아와서 써야한다.
            //'해당 중분류와 같은 대분류를 가진 중분류 목록'과 '선택한 중분류에 해당하는 소분류 목록'을 보여주려면 계속 null이어선 안된다. (둘다 middleId를 인자로 가진다)
            middleCategoryId = middleCategoryDto.getMiddleCategoryId();
            model.addAttribute("currentMiddleCategory", middleCategoryDto);
        }


        // 중분류와 소분류 목록 가져오기
        List<SmallCategoryDto> smallCategories = categoryService.findSmallsByMiddle(middleCategoryId); // 중복된 카테고리 로딩 제거
        model.addAttribute("smallCategories", smallCategories);

        List<MiddleCategoryDto> relatedMiddleCategories = categoryService.findRelatedMiddles(middleCategoryId); // 중복된 카테고리 로딩 제거
        model.addAttribute("relatedMiddleCategories", relatedMiddleCategories);


        int totalCnt = smallCategoryId != null ? productService.countProductBySmall(smallCategoryId) :
                productService.countProductByMiddle(middleCategoryId);

        //pageHandler에 계산된 totalCnt와 param으로 가져온 page,PasgeSize보내기
        PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);

        //페이징을 할때 사용할 offset과 pageSize, middleCategory 정보를 map에 저장
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("offset", ((page - 1) * pageSize));
        paramMap.put("pageSize", pageSize);
        paramMap.put("middleCategoryId", middleCategoryId);
        paramMap.put("smallCategoryId", smallCategoryId);


        //sort된 상품 리스트를 담을 list
        List<ProductWithMiddleSmallDto> list;

        //RequestParam으로 받은 정렬옵션에 따른 상품 목록(list)을 switch로 조절
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
                list = productService.sortByRating(paramMap);
                break;
            default: //기본 정렬
                list = productService.sortDefault(paramMap);
                break;
        }


        model.addAttribute("list", list);
        model.addAttribute("pageHandler", pageHandler);
        model.addAttribute("sortOption", sortOption);
        model.addAttribute("smallCategories", smallCategories);
        model.addAttribute("relatedMiddleCategories", relatedMiddleCategories);
        model.addAttribute("smallCategoryId", smallCategoryId);
        model.addAttribute("middleCategoryId", middleCategoryId);


        return "product/productList";

    }


}
