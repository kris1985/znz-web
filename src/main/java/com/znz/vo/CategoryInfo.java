package com.znz.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */
@Data
public class CategoryInfo {
    private Integer id;
    private String name;
    private Integer sortId;
    private boolean brandFlag;
    private List<ReferrerInfo> referrerInfos;
    private List<CategoryInfo> childrens;
}
