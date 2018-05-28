package com.znz.vo;

import lombok.Data;

/**
 * Created by Administrator on 2017/3/6.
 */
@Data
public class QueryParam {

    private  String firstSelectedId;
    private String secondSelectedId;
    private String thirdSelectedId;
    private String fourthSelectedId;
    private Integer currentPage;
    private Integer pageSize;
    private String startTime;
    private String endTime;
    private String delFlag;
    private Integer recommendId;
    private Integer brandId;
    private String brandName;
}
