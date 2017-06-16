package com.znz.vo;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * Created by Administrator on 2017/5/13.
 */
@Data
public class BaseRequest<T> {

    private String token;

    private String imei;

    private String v;

    private String sign;

    private String appVersion;

    private String timestamp;

    private T data;

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString("32"));
    }


}
