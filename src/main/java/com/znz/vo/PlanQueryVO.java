package com.znz.vo;

import lombok.Data;

/**
 * Created by admin on 2016/4/18.
 */
@Data
public class PlanQueryVO {

    private String name;
    private String hyrq;
    private String mdd;
    private String pt;
    private String lx;
    private String jhrq;
    private String sord;// desc or aes
    private String sortName;
    private PageParameter page;
}
