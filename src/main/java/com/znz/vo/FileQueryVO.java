package com.znz.vo;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/9.
 */

@Data
public class FileQueryVO {

    private String name;

    private List<String> categoryConditions;

    private PageParameter page;

}
