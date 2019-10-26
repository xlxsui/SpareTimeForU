package com.sparetimeforu.android.sparetimeforu.entity;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/17.
 * Email:17wjli6@stu.edu.cn
 */
public class Pagination {
    private boolean has_next = true;
    private boolean has_prev = false;
    private int next_num = 2;
    private int prev_num = 0;
    private int page = 1;
    private int pages = 0;
    private int per_page = 10;
    private int total = 100;

    public boolean isHas_next() {
        return has_next;
    }

    public void setHas_next(boolean has_next) {
        this.has_next = has_next;
    }

    public boolean isHas_prev() {
        return has_prev;
    }

    public void setHas_prev(boolean has_prev) {
        this.has_prev = has_prev;
    }

    public int getNext_num() {
        return next_num;
    }

    public void setNext_num(int next_num) {
        this.next_num = next_num;
    }

    public int getPrev_num() {
        return prev_num;
    }

    public void setPrev_num(int prev_num) {
        this.prev_num = prev_num;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "has_next=" + has_next +
                ", has_prev=" + has_prev +
                ", next_num=" + next_num +
                ", prev_num=" + prev_num +
                ", page=" + page +
                ", pages=" + pages +
                ", per_page=" + per_page +
                ", total=" + total +
                '}';
    }
}
