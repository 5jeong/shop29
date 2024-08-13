package com.toy2.shop29.product.dao;

import com.toy2.shop29.product.domain.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ProductDaoImplTest {

    @Autowired
    ProductDao productDao;


    @BeforeEach // 각 테스트가 수행되기 직전에 이 메서드가 실행된다.
    public void init() {
        productDao.deleteAll();
    }

//
//    @Test
//    public void select() {
//        ProductDto product1 = new ProductDto(1, 1,"brand1", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
//        ProductDto product2 = new ProductDto(2, 2,"brand2", "name2", 10000,20,1,new Date(),"aaa",new Date(),"aaa");
//
//
//        //select로 product 여러개 넣어주고
//        productDao.insert(product1);
//        productDao.insert(product2);
//
//        //특정 상품의 id를 select하면 같은 객체가 나오는지 확인
//        ProductDto dto = productDao.select(product1.getProduct_id());
//        assertEquals(dto,product1);
//    }


    //이상하게도 클래스 이름을 select로 하고 돌리면 무한 버퍼링이 생긴다. 신기하게 이름 바꿔주니까 그런거 싹 사라짐
    @Test
    public void select_1() {
        ProductDto product1 = new ProductDto(1, 1,"brand1", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product2 = new ProductDto(2, 2,"brand2", "name2", 10000,20,1,new Date(),"aaa",new Date(),"aaa");


        //select로 product 여러개 넣어주고
        productDao.insert(product1);
        productDao.insert(product2);

        //특정 상품의 id를 select하면 같은 객체가 나오는지 확인
        ProductDto dto = productDao.select(product1.getProduct_id());
        assertEquals(dto,product1);
    }

    @Test
    public void insert() {

        ProductDto product1 = new ProductDto(1, 1,"brand1", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product2 = new ProductDto(2, 2,"brand2", "name2", 10000,20,1,new Date(),"aaa",new Date(),"aaa");

        //BeforeEach에서 deleteAll해주기 때문에 insert한 횟수대로 count가 나와야한다
        //하나 넣어주면 count 1
        productDao.insert(product1);
        assertTrue(productDao.count()==1);

        //새로운 product 하나 더 넣어서 count 2
        productDao.insert(product2);
        assertTrue(productDao.count()==2);

    }

    @Test
    void update() {
        ProductDto product1 = new ProductDto(1, 1,"brand1", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product2 = new ProductDto(2, 1,"brand1", "name2", 10000,0,0,new Date(),"aaa",new Date(),"aaa");

        // 서로 이름만 다른 상품 2개를 insert하고
        productDao.insert(product1); //얘는 name1
        productDao.insert(product2); //얘는 name2

        //한 상품의 이름을 다른 상품의 이름과 동일하게 바꿔서
        product1.setProduct_name("name2");

        //이름이 잘 바뀌었는지 확인하고
        assertTrue(product1.getProduct_name()=="name2");

        //모든 값이 다 동일할지라도 PK인 product_id가 다르면 다른 객체로 인식하는지 확인
        assertTrue(!product1.equals(product2));

    }

    @Test
    public void delete() {
        ProductDto product1 = new ProductDto(1, 1,"brand1", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product2 = new ProductDto(2, 1,"brand1", "name2", 10000,0,0,new Date(),"aaa",new Date(),"aaa");

        //product 2개를 올리고
        productDao.insert(product1);
        productDao.insert(product2);

        //그 중 하나를 지워서
        productDao.delete(product1.getProduct_id());

        //count가 1 나오는지 확인
        assertTrue(1==productDao.count());

        //나머지 하나 더 지우고 count 0 나오는지 확인
        productDao.delete(product2.getProduct_id());
        assertTrue(0==productDao.count());

    }

    @Test
    public void deleteByBrand(){
        ProductDto product1 = new ProductDto(1, 1,"brand1", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product2 = new ProductDto(2, 1,"brand2", "name2", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product3 = new ProductDto(3, 1,"brand1", "name3", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product4 = new ProductDto(4, 1,"brand2", "name4", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product5 = new ProductDto(5, 1,"brand1", "name5", 10000,0,0,new Date(),"aaa",new Date(),"aaa");


        //brand_id 여러개 섞어서 product를  insert해주고 총 몇개인지 센다.
        productDao.insert(product1);
        productDao.insert(product2);
        productDao.insert(product3);
        productDao.insert(product4);
        productDao.insert(product5);

        int allCnt = productDao.count(); //5개

        //그 중 product1과 brand_id가 같은 product들의 갯수를 센다
        int brandCnt = productDao.brandCount(product1.getBrand_id()); //3개

        //product1의 brand_id와 같은 product들을 모두 삭제하고
        productDao.deleteByBrand(product1.getBrand_id());

        //갯수를 다시 세봤을때 allCnt - brandCnt 가 나와야 하고
        assertEquals(productDao.count(),allCnt-brandCnt);

        //selectAll하고 하나씩 상품 꺼내서 그 상품들의 brand가 product1의 brand와 다른 브랜드만 있으면, 같은 브랜드였던 상품들은 다 삭제가 된 것
        List<ProductDto> list = productDao.selectAll();

        for(ProductDto dto : list){
            assertTrue(dto.getBrand_id()!=product1.getBrand_id());
        }
    }


    @Test
    void deleteAll() {
        ProductDto product1 = new ProductDto(1, 1,"brand2", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
        ProductDto product2 = new ProductDto(2, 1,"brand1", "name2", 10000,0,0,new Date(),"aaa",new Date(),"aaa");

        //product 2개 넣으면
        productDao.insert(product1);
        productDao.insert(product2);

        //카운트가 2개 나와야하고
        assertEquals(2, productDao.count());

        //deleteAll로 전부 삭제시키면
        productDao.deleteAll();

        //0개 나와야한다
        assertEquals(0, productDao.count());
    }


    @Test
    void count() {
        ProductDto product1 = new ProductDto(1, 1,"brand2", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");

        //product 1개 넣어줬을때
        productDao.insert(product1);

        //count 1개 나와야 하고
        assertEquals(1, productDao.count());

        //그 상태에서 다시 빼면
        productDao.delete(product1.getProduct_id());

        //count 0개
        assertEquals(0, productDao.count());
    }

    @Test
    public void selectAll() {

        ProductDto product1 = new ProductDto(1, 1, "brand2", "name1", 10000, 0, 0, new Date(), "aaa", new Date(), "aaa");
        ProductDto product2 = new ProductDto(2, 1, "brand1", "name2", 10000, 0, 0, new Date(), "aaa", new Date(), "aaa");
        ProductDto product3 = new ProductDto(3, 1, "brand2", "name3", 10000, 0, 0, new Date(), "aaa", new Date(), "aaa");
        ProductDto product4 = new ProductDto(4, 1, "brand1", "name4", 10000, 0, 0, new Date(), "aaa", new Date(), "aaa");
        ProductDto product5 = new ProductDto(5, 1, "brand2", "name5", 10000, 0, 0, new Date(), "aaa", new Date(), "aaa");

        //product 5개 넣기
        productDao.insert(product1);
        productDao.insert(product2);
        productDao.insert(product3);
        productDao.insert(product4);
        productDao.insert(product5);

        //selectAll 해서 list의 size 확인 했을때 5가 나와야한다
        assertEquals(5, productDao.selectAll().size());

        //delete해서 2개 빼면
        productDao.delete(product1.getProduct_id());
        productDao.delete(product2.getProduct_id());

        //다시 selectAll했을때 size가 3이 나와야한다
        assertEquals(3, productDao.selectAll().size());

    }

        @Test
        public void BrandCount(){
            ProductDto product1 = new ProductDto(1, 1,"brand1", "name1", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
            ProductDto product2 = new ProductDto(2, 1,"brand2", "name2", 10000,0,0,new Date(),"aaa",new Date(),"aaa");
            ProductDto product3 = new ProductDto(3, 1,"brand1", "name3", 10000,0,0,new Date(),"aaa",new Date(),"aaa");



            //brand_id가 brand1인 product와 brand2인 product 추가
            productDao.insert(product1); //brand1
            productDao.insert(product2); //brand2

            //BrandCount해서 brand1인 상품의 갯수가 1, brand2인 상품의 갯수가 1 나와야 한다
            assertEquals(1,productDao.brandCount("brand1"));
            assertEquals(1,productDao.brandCount("brand2"));

            //brand1인 또 다른 제품이 하나 더 추가되면
            productDao.insert(product3);

            //brand1의 BrandCount는 2가 나와야 한다
            assertEquals(2,productDao.brandCount("brand1"));

        }

    }
