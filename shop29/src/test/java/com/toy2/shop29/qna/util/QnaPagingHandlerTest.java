package com.toy2.shop29.qna.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QnaPagingHandlerTest {

    @Test
    public void 전체_페이지수_계산(){
        QnaPagingHandler ph1 = new QnaPagingHandler(
                35,10,10
        );

        assertTrue(ph1.getTotalPageCnt() == 4);

        QnaPagingHandler ph2 = new QnaPagingHandler(
                35,5,10
        );

        assertTrue(ph2.getTotalPageCnt() == 7);

        QnaPagingHandler ph3 = new QnaPagingHandler(
                41,10,10
        );

        assertTrue(ph3.getTotalPageCnt() == 5);
    }

    @Test
    public void 하단_목록_가장_앞페이지_번호(){
        QnaPagingHandler ph1 = new QnaPagingHandler(
                35,10,10
        );

        // 35개의 게시물에 대해, 하단 목록 수가 10이고,
        // 보여지는 게시물이 10개 이므로,
        // 현재 페이지가 1일 때, 1임
        assertTrue(ph1.getFirstPage() == 1);

        QnaPagingHandler ph2 = new QnaPagingHandler(
                350,10,10,15
        );

        // 350개의 게시물에 대해, 하단 목록 수가 10이고,
        // 보여지는 게시물이 10개 이므로,
        // 현재 페이지가 15일 때, 첫페이지는 11
        assertTrue(ph2.getFirstPage() == 11);

        QnaPagingHandler ph3 = new QnaPagingHandler(
                350,10,10,40
        );

        // 350개의 게시물에 대해, 하단 목록 수가 10이고,
        // 보여지는 게시물이 10개 이므로,
        // 현재 페이지가 40일 때, 첫페이지는 31
        assertTrue(ph3.getFirstPage() == 31);
    }

    @Test
    public void 하단_목록_가장_끝페이지_번호(){
        QnaPagingHandler ph1 = new QnaPagingHandler(
                350,10,10,15
        );

        assertTrue(ph1.getLastPage() == 20);

        QnaPagingHandler ph2 = new QnaPagingHandler(
                350,10,10,40
        );

        assertTrue(ph2.getLastPage() == 35);
    }

    @Test
    public void 하단_목록_앞페이지_번호(){
        QnaPagingHandler ph1 = new QnaPagingHandler(
                35,5,5,5
        );

        assertTrue(ph1.getFirstPage() == 1);

        QnaPagingHandler ph2 = new QnaPagingHandler(
                35,5,5,6
        );

        assertTrue(ph2.getFirstPage() == 6);
    }

    @Test
    public void 하단_목록_뒤페이지_번호(){
        QnaPagingHandler ph1 = new QnaPagingHandler(
                35,5,5,5
        );

        assertTrue(ph1.getLastPage() == 5);

        QnaPagingHandler ph2 = new QnaPagingHandler(
                35,5,5,6
        );

        assertTrue(ph2.getLastPage() == 7);
    }
}