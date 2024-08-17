package com.toy2.shop29.product.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class PageHandler {
    private int totalCnt;
    private int pageSize;
    private int naviSize = 5;
    private int totalPage;
    private int page;
    private int beginPage;
    private int endPage;
    private boolean showPrev;
    private boolean showNext;


    //생성자
    public PageHandler(Integer totalCnt, Integer page) {
        this(totalCnt, page, 50);
    }

    public PageHandler(Integer totalCnt, Integer page, Integer pageSize) {
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;

        totalPage = (int) Math.ceil(totalCnt / (double) pageSize);
        int halfNavi = naviSize / 2; //2
        this.beginPage = (page <= halfNavi) ? 1 : page - halfNavi;
        if (this.beginPage + naviSize - 1 > totalPage) {
            this.beginPage = Math.max(1, totalPage - naviSize + 1);
        }
        this.endPage = Math.min(this.beginPage + naviSize - 1, totalPage);


        showPrev = beginPage != 1;
        showNext = endPage != totalPage;

    }

    public void print() {
        System.out.println("page = " + page);
        System.out.print(showPrev ? "[Prev]" : "");
        for (int i = beginPage; i <= endPage; i++) {
            System.out.print(i + " ");
        }
        System.out.println(showNext ? "[Next]" : "");
    }


}