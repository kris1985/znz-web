package com.znz.vo;

import lombok.Data;

/**
 * Created by admin on 2016/4/18.
 */
@Data
public class TravelZyxQueryVO {

    private String pch;
    private String days;
    private String cpid;
    private String cfd;
    private String mdd;
    private String jtfs;
    private String sord;// desc or aes
    private String sortName;
    private PageParameter page;
}
