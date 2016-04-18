package com.znz.vo;

import lombok.Data;

/**
 * Created by Administrator on 2015/2/1.
 */
public class ResultVO {
    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private int code = -1;
    private String msg;
}
