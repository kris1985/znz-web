package com.znz.vo;

import lombok.Data;

/**
 * Created by admin on 2016/4/18.
 */
@Data
public class AttractionsQueryVO {

    private String prodName;
    private String sortName = "id";
    private PageParameter page;
}
