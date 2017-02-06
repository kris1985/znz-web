package com.znz.vo;

import lombok.Data;

/**
 * Created by Administrator on 2017/2/6.
 */

@Data
public class SubCategoryVO {

    private String id;

    private String name;

    private Integer sortId;

    private Integer parentId;

    private String parentName;

    private String oper;
}
