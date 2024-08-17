package com.toy2.shop29.product.dao.category;

import com.toy2.shop29.product.domain.category.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CategoryDaoImpl implements CategoryDao {


    @Autowired
    private SqlSession session;

    String namespace = "com.toy2.shop29.product.dao.CategoryMapper.";


    //메인페이지에서 사용
    //모든 Major카테고리들 List로 가져오기
    @Override
    public List<MajorCategoryDto> findAllMajorCategories(){
        return session.selectList(namespace+"findAllMajorCategories");
    }

    //MajorId에 해당하는 middle 가져오기
    public List<MajorMiddleDto> getMiddleByMajor(int majorCategoryId) {
        return session.selectList(namespace+"getMiddleByMajor", majorCategoryId);
    }

    @Override
    public List<SmallCategoryDto> getSmallCategoriesByMiddle(int middleCategoryId) {
        return session.selectList(namespace+ "getSmallCategoriesByMiddle", middleCategoryId);
    }

    //같은 대분류를 가지는 중분류 리스트 반환
    @Override
    public List<MiddleCategoryDto> getRelatedMiddleCategories(int middleCategoryId) {
        return session.selectList(namespace+ "getRelatedMiddleCategories", middleCategoryId);
    }

    @Override
    public MiddleCategoryDto getMiddleCategoryBySmall(int smallCategoryId){
        return session.selectOne(namespace+ "getMiddleCategoryBySmall", smallCategoryId);
    }











    @Override
    public List<MiddleCategoryDto> findAllMiddleCategories() {
        return session.selectList(namespace + "findAllMiddleCategories");
    }



    // 대분류, 중분류, 소분류 전체 가져오기
    @Override
    public List<CategoryDto> findAllCategories() {
        return session.selectList(namespace + "findAllCategories");
    }


//
//    //특정 MajorId에 해당하는 모든 Middle카테고리를 List로 가져오기
//    @Override
//    public List<MiddleCategoryDto> findMiddleCategoriesByMajorId(int majorCategoryId) {
//        return session.selectList(namespace + "findMiddleCategoriesByMajorId", majorCategoryId);
//    }
//
//    //특정 MiddleId에 해당하는 모든 Small카테고리를 List로 가져오기
//    @Override
//    public List<SmallCategoryDto> findSmallCategoriesByMiddleId(int middleCategoryId) {
//        return session.selectList(namespace + "findSmallCategoriesByMiddleId", middleCategoryId);
//    }
//

//    //소분류Id에 속한 모든 상품 가져오기
//    @Override
//    public List<ProductDto> findProductsBySmallId(int smallCategoryId){
//        return session.selectList(namespace + "findProductsBySmallId", smallCategoryId);
//    }
//
    @Override
    public List<CategoryDto> findMajorMiddle(){
        return session.selectList(namespace + "findMajorMiddle");
    }


    //select - 1 by categoryDto
//    @Override
//    public CategoryDto selectCategoryByCategoryDto(CategoryDto categoryDto) {
//        return session.selectOne(namespace + "getCategoryHierarchy", categoryDto);
//    }

    //select - 1 by ID
    @Override
    public CategoryDto selectCategorybyId(int majorCategoryId, int middleCategoryId, int smallCategoryId) {
        return session.selectOne(namespace + "selectCategory",
                Map.of("majorCategoryId", majorCategoryId, "middleCategoryId", middleCategoryId, "smallCategoryId", smallCategoryId));
    }


    @Override
    public int insertMajorCategory(MajorCategoryDto majorCategoryDto) {
        return session.insert(namespace + "insertMajorCategory", majorCategoryDto);
    }

    @Override
    public int insertMiddleCategory(MiddleCategoryDto middleCategoryDto) {
        return session.insert(namespace + "insertMiddleCategory", middleCategoryDto);
    }

    @Override
    public int insertSmallCategory(SmallCategoryDto smallCategoryDto) {
        return session.insert(namespace + "insertSmallCategory", smallCategoryDto);
    }



    @Override
    public int updateMajorCategory(MajorCategoryDto majorCategoryDto) {
        return session.update(namespace + "updateMajorCategory", majorCategoryDto);
    }

    @Override
    public int updateMiddleCategory(MiddleCategoryDto middleCategoryDto) {
        return session.update(namespace + "updateMiddleCategory", middleCategoryDto);
    }

    @Override
    public int updateSmallCategory(SmallCategoryDto smallCategoryDto) {
        return session.update(namespace + "updateSmallCategory", smallCategoryDto);
    }



    @Override
    public int deleteMajorCategory(int majorCategoryId) {
        return session.delete(namespace + "deleteMajorCategory", majorCategoryId);
    }

    @Override
    public int deleteMiddleCategory(int middleCategoryId) {
        return session.delete(namespace + "deleteMiddleCategory", middleCategoryId);
    }

    @Override
    public int deleteSmallCategory(int smallCategoryId) {
        return session.delete(namespace + "deleteSmallCategory", smallCategoryId);
    }



    @Override
    public int deleteAllCategories() {
        return session.delete(namespace + "deleteAllCategories");
    }






//    //insert - 1
//    @Override
//    public int insertCategory(CategoryDto categoryDto) {
//        return session.insert(namespace + "insertCategory", categoryDto);
//    }
//
//    //update - 1
//    @Override
//    public int updateCategory(CategoryDto categoryDto) {
//        return session.update(namespace + "updateCategory", categoryDto);
//    }
//
//    //delete - 1 by Id
//    @Override
//    public int deleteCategory(int majorCategoryId, int middleCategoryId, int smallCategoryId) {
//        return session.delete(namespace + "deleteCategory", new CategoryDto(majorCategoryId, middleCategoryId, smallCategoryId));
//    }
//
//    //delete - all
//    @Override
//    public int deleteAll() {
//        return session.delete(namespace + "deleteAll");
//    }
//
//    //count
//    @Override
//    public int countCategories() {
//        return session.selectOne(namespace + "countCategory");


    @Override
    public int countMiddleCategory(int middleCategoryId) {
        return session.selectOne(namespace + "countMiddleCategory", middleCategoryId);
    }

    @Override


    public MiddleCategoryDto findMiddleCategoryById(int middleCategoryId) {
        return session.selectOne(namespace + "findMiddleCategoryById", middleCategoryId);
    }

    @Override
    public List<MiddleCategoryDto> findMiddleCategoriesByMajorCategoryId(int majorCategoryId) {
        return session.selectList(namespace + "findMiddleCategoriesByMajorCategoryId", majorCategoryId);
    }



    }










