package com.toy2.shop29.product.controller;

import com.toy2.shop29.product.domain.MiddleCategoryDto;
import com.toy2.shop29.product.domain.PageHandler;
import com.toy2.shop29.product.domain.ProductDto;
import com.toy2.shop29.product.service.CategoryService;
import com.toy2.shop29.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testListPage() {
        // Given
        int middleCategoryId = 1; // 테스트에 사용될 중분류 ID
        int page = 1;
        int pageSize = 50;

        // 파라미터 맵을 생성합니다.
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("middleCategoryId", middleCategoryId);
        paramMap.put("offset", (page - 1) * pageSize);
        paramMap.put("pageSize", pageSize);

        // 실제 서비스 메서드를 호출하여 데이터를 가져옵니다.
        int totalCnt = productService.getCountByMiddleCategory(middleCategoryId);
        List<ProductDto> productList = productService.getPageByMiddleCategory(paramMap);
//        List<MiddleCategoryDto> relatedMiddleCategories = categoryService.getRelatedMiddleCategories(middleCategoryId);

        // Model 객체를 생성
        Model model = new BindingAwareModelMap();

        // When
        String viewName = productController.list(page, pageSize, "", middleCategoryId, model);

        // Then
        assertThat(viewName).isEqualTo("product/productList");
//        assertThat(model.containsAttribute("list")).isTrue();
//        assertThat(model.containsAttribute("pageHandler")).isTrue();
//        assertThat(model.containsAttribute("sortOption")).isTrue();
//        assertThat(model.containsAttribute("middleCategories")).isTrue();

//        // 모델에 담긴 데이터 확인
//        assertThat(model.getAttribute("list")).isEqualTo(productList);
//        PageHandler pageHandler = (PageHandler) model.getAttribute("pageHandler");
//        assertThat(pageHandler.getTotalCnt()).isEqualTo(totalCnt);
//        assertThat(model.getAttribute("middleCategories")).isEqualTo(relatedMiddleCategories);
    }
}
