package com.toy2.shop29.product.domain.category;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDto {
    // Getters and Setters
    private MajorCategoryDto majorCategory;
    private MiddleCategoryDto middleCategory;
    private SmallCategoryDto smallCategory;



    public CategoryDto() {
    }

    public CategoryDto(int majorCategoryId, int middleCategoryId, int smallCategoryId) {
        this.majorCategory = new MajorCategoryDto();
        this.majorCategory.setMajorCategoryId(majorCategoryId);

        this.middleCategory = new MiddleCategoryDto();
        this.middleCategory.setMiddleCategoryId(middleCategoryId);
        this.middleCategory.setMajorCategoryId(majorCategoryId); // 필요한 경우 추가

        this.smallCategory = new SmallCategoryDto();
        this.smallCategory.setSmallCategoryId(smallCategoryId);
        this.smallCategory.setMiddleCategoryId(middleCategoryId); // 필요한 경우 추가
    }

}