package com.znz.vo;

import lombok.Data;

/**
 * Created by admin on 2016/4/18.
 */
@Data
public class UserQueryVO {

    private String userName;
    private String company;
    private String sortName = "userId";
    private PageParameter page;
}
