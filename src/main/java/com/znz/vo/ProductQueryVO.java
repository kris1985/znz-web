package com.znz.vo;

import lombok.Data;

/**
 * Created by admin on 2016/4/18.
 */
@Data
public class ProductQueryVO {

    private Integer id;

    private String prodName;

    private String starting;

    private String destination;

    private String sortName = "id";

    private PageParameter page;
}
