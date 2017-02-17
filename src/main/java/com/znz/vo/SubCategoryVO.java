package com.znz.vo;

import lombok.Data;

import java.util.List;

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

    private Integer categoryLevel;

    private String allFlag;

    private List<SubCategoryVO> childrens;
}
