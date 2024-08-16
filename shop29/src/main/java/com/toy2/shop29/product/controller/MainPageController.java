package com.toy2.shop29.product.controller;

import com.toy2.shop29.product.domain.CategoryDto;
import com.toy2.shop29.product.domain.MajorCategoryDto;
import com.toy2.shop29.product.domain.MiddleCategoryDto;
import com.toy2.shop29.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {

    
    @Autowired
    private CategoryService categoryService;
    

    @GetMapping("/")
    public String showMainPage(Model model) {

        Map<MajorCategoryDto, List<MiddleCategoryDto>> majorToMiddleMap = categoryService.getMajorToMiddleCategoryMap();


        model.addAttribute("majorToMiddleMap", majorToMiddleMap);

        
        return "product/mainpage";
    }

}