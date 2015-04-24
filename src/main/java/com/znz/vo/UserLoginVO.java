package com.znz.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by huangtao on 2015/1/23.
 */

public class UserLoginVO {

    private  String userName;

    private String pwd;

    private int remember;// 1 记住密码

    private String randomCode;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setRemember(int remember) {
        this.remember = remember;
    }

    public int getRemember() {
        return remember;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public String getRandomCode() {
        return randomCode;
    }

    public static  void main(String args[]){
        System.out.println("fd\\d".replaceAll("\\\\","/"));
    }


}
