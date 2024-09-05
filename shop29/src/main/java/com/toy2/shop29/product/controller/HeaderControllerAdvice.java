package com.toy2.shop29.product.controller;

import com.toy2.shop29.product.domain.category.MajorCategoryDto;
import com.toy2.shop29.product.domain.category.MajorMiddleDto;
import com.toy2.shop29.product.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class HeaderControllerAdvice {

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute
    public void addAttributes(Model model) {

        // 모든 대분류 가져오기
        List<MajorCategoryDto> majorCategories = categoryService.findAllMajors();

        // 각 대분류에 속한 중분류를 매핑
        Map<MajorCategoryDto, List<MajorMiddleDto>> majorWithMiddleCategories = new LinkedHashMap<>();
        for (MajorCategoryDto major : majorCategories) {
            List<MajorMiddleDto> majorMiddles = categoryService.findMiddleMajor(major.getMajorCategoryId());
            majorWithMiddleCategories.put(major, majorMiddles);
        }

        // 모델에 데이터 추가
        model.addAttribute("majorWithMiddleCategories", majorWithMiddleCategories);

    }
}