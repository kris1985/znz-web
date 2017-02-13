package com.znz.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2017/2/9.
 */

@Data
public class FileQueryVO {

    private String name;

    private List<Long> subCategoryIds;

    private PageParameter page;

}
