package com.znz.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by huangtao on 2015/1/23.
 */

public class UserLoginVO {

    public String getUserName() {
        return userName;
    }

    private  String userName;

    private String pwd;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }



    public String getPwd() {
        return pwd;
    }

}
