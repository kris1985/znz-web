package com.znz.vo;

import lombok.Data;

/**
 * Created by admin on 2016/4/18.
 */
@Data
public class TravelGtyQueryVO {

    private String gys;
    private String cpmc;
    private String days;
    private String pc;
    private String cfd;
    private String mdd;
    private String pid;
    private String jtfs;
    private String sord;// desc or aes
    private String sortName;
    private PageParameter page;
}
