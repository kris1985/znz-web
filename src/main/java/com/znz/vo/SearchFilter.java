package com.znz.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by admin on 2016/4/19.
 */
@Data
public class SearchFilter {

    private String groupOp;

    private List<SearchField> rules;
}
