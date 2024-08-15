package com.toy2.shop29.product.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MajorCategoryDaoTest {

    @Autowired
    private MajorCategoryDao dao;





    @Test
    void selectAll() {
    }

    @Test
    void selectById() {
        assertTrue(dao!=null);
        System.out.println("majorCategoryDao = " + dao);
////        dao.selectById(1);

    }

    @Test
    void insert() {

    }

    @Test
    void delete() {

    }

    @Test
    void selectByName() {
    }
}