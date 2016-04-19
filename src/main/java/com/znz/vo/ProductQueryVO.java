package com.znz.vo;

import lombok.Data;

/**
 * Created by admin on 2016/4/18.
 */
@Data
public class ProductQueryVO {

    private String prodNo;

    private String prodSort;

    private String prodSale;

    private String prodName;

    private String start;

    private String destination;

    private String sortName = "id";

    private String sord;

    private PageParameter page;
}
