package com.toy2.shop29.product.controller;

import com.toy2.shop29.product.domain.MajorCategoryDto;
import com.toy2.shop29.product.domain.MiddleCategoryDto;
import com.toy2.shop29.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MainPageControllerTest {

    @Autowired
    private MainPageController mainPageController;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testShowMainPage() {
        // Model 객체를 생성합니다.
        Model model = new BindingAwareModelMap();

        // 컨트롤러 메서드를 호출합니다.
        String viewName = mainPageController.showMainPage(model);

        // 뷰 이름이 올바른지 확인합니다.
        assertThat(viewName).isEqualTo("product/mainpage");

        // Model에 majorToMiddleMap 속성이 추가되었는지 확인합니다.
        assertThat(model.containsAttribute("majorToMiddleMap")).isTrue();

        // majorToMiddleMap의 실제 데이터를 확인합니다.
        Map<MajorCategoryDto, List<MiddleCategoryDto>> majorToMiddleMap =
                (Map<MajorCategoryDto, List<MiddleCategoryDto>>) model.getAttribute("majorToMiddleMap");

        assertThat(majorToMiddleMap).isNotEmpty();

        // 필요에 따라 더 상세한 검증 추가 가능
    }
}
