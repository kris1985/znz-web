package com.znz.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by huangtao on 2015/1/23.
 */
@Data
public class UserLoginVO {
    @NotBlank
    private  String userName;
    @NotBlank
    private String pwd;
}
