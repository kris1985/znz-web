package com.znz.vo;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/13.
 */
@Data
public class QueryParams {
    private Set<Integer> categoryIds;
    private Integer referrerId;
    private Integer currentPage;
    private Integer pageSize;
}
