package com.toy2.shop29.qna.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaPagingHandler {

    public QnaPagingHandler(int totalBoardCnt, int showBoardCnt, int bottomPageCnt){
        this(totalBoardCnt,showBoardCnt,bottomPageCnt,1);
    }

    public QnaPagingHandler(int TBC, int SBC, int BPC, int CP){
        totalBoardCnt = TBC;
        showBoardCnt = SBC;
        bottomPageCnt = BPC;
        curPage = CP;

        // 1. 전체 페이지 수 계산
        totalPageCnt = (totalBoardCnt / showBoardCnt) + (totalBoardCnt % showBoardCnt == 0 ? 0 : 1);

        // 1-1. 현재 페이지 계산
        if(curPage <= 0){
            curPage = 1;
        } else if (curPage > totalPageCnt) {
            curPage = totalPageCnt;
        }

        // 2. 가장 앞페이지 번호
        firstFirstPage = 1;

        // 3. 가장 끝페이지 번호
        lastLastPage = totalPageCnt;

        // 4. 현재 페이지 번호를 기준으로, 하단 목록 가장 앞페이지 번호
        firstPage = (curPage % bottomPageCnt == 0 ? (curPage / bottomPageCnt) - 1 : curPage / bottomPageCnt ) * bottomPageCnt + 1;

        // 5. 현재 페이지 번호를 기준으로, 하단 목록 가장 끝페이지 번호
        //
        lastPage = ((curPage % bottomPageCnt == 0 ? (curPage / bottomPageCnt) - 1 : curPage / bottomPageCnt ) + 1) * showBoardCnt;


        lastPage = lastPage < lastLastPage ? lastPage : lastLastPage;
    }

    private int totalBoardCnt; // 전체 게시글 수
    private int showBoardCnt; // 표출될 게시글 수
    private int bottomPageCnt; // 하단 페이징 목록 수

    private int totalPageCnt; // 전체 페이지 수
    private int firstFirstPage; // 가장 앞페이지 번호
    private int lastLastPage; // 가장 끝페이지 번호

    private int firstPage; // 하단 목록에서, 가장 앞페이지 번호
    private int lastPage; // 하단 목록에서, 가장 끝페이지 번호

    private int curPage; // 현재 페이지 번호
}
