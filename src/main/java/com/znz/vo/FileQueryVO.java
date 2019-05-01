package com.znz.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/9.
 */

@Data
public class FileQueryVO {

    private String name;

    private List<Set<Integer>> categoryConditions;

    private PageParameter page;

    private Date startTime;

    private Date endTime;

    private Integer recommendId;

    private Integer partionCode;

    private String sortFiled;

    private Long bookId;

}
