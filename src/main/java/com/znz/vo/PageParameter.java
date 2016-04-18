package com.znz.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 分页参数类
 * 
 */
@Data
public class PageParameter implements Serializable{

    public static final int DEFAULT_CURRENT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;


    private int pageSize; //每页记录数
    private int currentPage; //当前页数
    private int totalCount; //总记录数

    public PageParameter() {
        this.currentPage = DEFAULT_CURRENT_PAGE;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     * 
     * @param currentPage
     * @param pageSize
     */
    public PageParameter(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
