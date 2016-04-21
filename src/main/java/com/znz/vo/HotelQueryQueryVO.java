package com.znz.vo;

import lombok.Data;

/**
 * Created by admin on 2016/4/18.
 */
@Data
public class HotelQueryQueryVO {

    private String hotelName;
    private String areaName;
    private String xj;
    private String sord;
    private String sortName;
    private PageParameter page;
}
