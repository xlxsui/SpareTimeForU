package com.sparetimeforu.android.sparetimeforu.entity;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SpareTimeForU
 * Created by Jin on 2019/10/17.
 * Email:17wjli6@stu.edu.cn
 */
public class PaginationTest {

    @Test
    public void isHas_next() {
    }

    @Test
    public void setHas_next() {
    }

    @Test
    public void isHas_prev() {
    }

    @Test
    public void setHas_prev() {
    }

    @Test
    public void getNext_num() {
    }

    @Test
    public void setNext_num() {
    }

    @Test
    public void getPrev_num() {
    }

    @Test
    public void setPrev_num() {
    }

    @Test
    public void getPage() {
    }

    @Test
    public void setPage() {
        Pagination pagination = new Pagination();
        assertEquals(1, pagination.getPage());
        assertEquals(2, pagination.getNext_num());

    }

    @Test
    public void getPages() {
    }

    @Test
    public void setPages() {
    }

    @Test
    public void getPer_page() {
    }

    @Test
    public void setPer_page() {
    }

    @Test
    public void getTotal() {
    }

    @Test
    public void setTotal() {
    }
}