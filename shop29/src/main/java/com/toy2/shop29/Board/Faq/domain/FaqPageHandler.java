package com.toy2.shop29.Board.Faq.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FaqPageHandler {
    private int totalCnt;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    // Constructor, getters, and setters
    public FaqPageHandler(int totalCnt, int currentPage, int pageSize) {
        this.totalCnt = totalCnt;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalCnt / pageSize);
    }

    public int getNumber() {
        return currentPage - 1;
    }

    public int getSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }
}
