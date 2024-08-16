package com.toy2.shop29.product.domain;

import java.util.Objects;

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
        this(totalCnt,page,50);
    }

    public PageHandler(Integer totalCnt, Integer page, Integer pageSize) {
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;

        totalPage = (int)Math.ceil(totalCnt/(double)pageSize);
        int halfNavi = naviSize/2; //2
        this.beginPage = (page <= halfNavi) ? 1 : page - halfNavi;
        if( this.beginPage + naviSize -1 > totalPage) {
            this.beginPage = Math.max(1, totalPage - naviSize +1);
        }
        this.endPage = Math.min(this.beginPage + naviSize -1 , totalPage);


        showPrev = beginPage !=1;
        showNext = endPage != totalPage;

    }

    public void print(){
        System.out.println("page = " + page);
        System.out.print(showPrev ? "[Prev]" : "");
        for(int i = beginPage; i <= endPage; i++){
            System.out.print(i+" ");
        }
        System.out.println(showNext ? "[Next]" : "");
    }





    //toString
    @Override
    public String toString() {
        return "PageHandler{" +
                "totalCnt=" + totalCnt +
                ", pageSize=" + pageSize +
                ", naviSize=" + naviSize +
                ", totalPage=" + totalPage +
                ", page=" + page +
                ", beginPage=" + beginPage +
                ", endPage=" + endPage +
                ", showPrev=" + showPrev +
                ", showNext=" + showNext +
                '}';
    }

    //equals hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageHandler that = (PageHandler) o;
        return totalCnt == that.totalCnt && pageSize == that.pageSize && naviSize == that.naviSize && totalPage == that.totalPage && page == that.page && beginPage == that.beginPage && endPage == that.endPage && showPrev == that.showPrev && showNext == that.showNext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCnt, pageSize, naviSize, totalPage, page, beginPage, endPage, showPrev, showNext);
    }


    //getter setter
    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNaviSize() {
        return naviSize;
    }

    public void setNaviSize(int naviSize) {
        this.naviSize = naviSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }


    public boolean isShowPrev() {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }
}
