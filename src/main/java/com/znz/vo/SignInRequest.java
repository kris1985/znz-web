package com.znz.vo;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Administrator on 2017/5/13.
 */
@Data
@ToString(callSuper = true)
public class SignInRequest  {
    private String userName;
    private String password;
}
