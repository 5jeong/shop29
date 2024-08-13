package com.toy2.shop29.Board.Notice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PageHandler {
    private int totalCnt; // 총 게시물 갯수
    private int pageSize; // 한 페이지의 크기
    private int naviSize=10;  //페이지 네비게이션의 크기
    private int totalPage;  //전체 페이지의 갯수
    private int currentPage; // 현재 페이지
    private int beginPage;  //네비게이션의 현재 페이지
    private int endPage;  //네비게이션의 마지막 페이지
    private boolean showPrev;   //이전 페이지로 이동하는 링크를 보여줄 것인지의 여부
    private boolean showNext;   //다음 페이지로 이동하는 링크를 보여줄 것인지의 여부

    public PageHandler(int totalCnt, int currentPage) {
        this(totalCnt, currentPage, 10);
    }

    public PageHandler(int totalCnt, int currentPage, int pageSize) {
        this.totalCnt = totalCnt;
        this.currentPage = currentPage;
        this.pageSize = pageSize;

        totalPage=(int)Math.ceil(totalCnt/(double)pageSize);
        beginPage= (currentPage-1)/naviSize*naviSize+1;
        endPage=Math.min(beginPage+naviSize-1, totalPage);
        showPrev=beginPage !=1;
        showNext=endPage !=totalPage;
    }
    public void print(){
        System.out.println("page= "+currentPage);
        System.out.print(showPrev ?"[PREV]":"");
        for(int i=beginPage; i<=endPage; i++){
            System.out.print(i+"");
        }
        System.out.println(showNext ?"[NEXT]" : "");



    }
}
