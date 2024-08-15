package com.toy2.shop29.product.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageHandlerTest {

    // 값이 내가 예상한대로 잘 들어가는지 확인
    @Test
    public void pageHandlerTest(){
        //(1) 2 3 4 5 나와야됨
        PageHandler ph = new PageHandler(250,1);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==1);
        assertTrue(ph.getEndPage()==5);

        //1 (2) 3 4 5 나와야됨
        ph = new PageHandler(250,2);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==1);
        assertTrue(ph.getEndPage()==5);

        //1 2 (3) 4 5 나와야됨
        ph = new PageHandler(250,3);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==1);
        assertTrue(ph.getEndPage()==5);

        //1 2 3 (4) 5 나와야됨
        ph = new PageHandler(250,4);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==1);
        assertTrue(ph.getEndPage()==5);

        //1 2 3 4 (5) 나와야됨
        ph = new PageHandler(250,5);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==1);
        assertTrue(ph.getEndPage()==5);

        //[prev] 2 3 4 (5) 6 나와야됨
        ph = new PageHandler(280,5);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==2);
        assertTrue(ph.getEndPage()==6);

        //[prev] 3 4 (5) 6 7 [next] 나와야됨
        ph = new PageHandler(490,5);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==3);
        assertTrue(ph.getEndPage()==7);

        //(1) 2 나와야됨
        ph = new PageHandler(70,1);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==1);
        assertTrue(ph.getEndPage()==2);

        //[prev] 16 17 18 19 (20) 나와야됨
        ph = new PageHandler(990,20);
        ph.print();
        System.out.println("ph = " + ph);
        assertTrue(ph.getBeginPage()==16);
        assertTrue(ph.getEndPage()==20);
    }
}