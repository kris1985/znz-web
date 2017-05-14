package com.znz.vo;

import lombok.Data;

/**
 * Created by Administrator on 2017/5/15.
 */
@Data
public class PageResponse extends CommonResponse {

    private Integer totalPage;

    private Integer totalCount;
}
