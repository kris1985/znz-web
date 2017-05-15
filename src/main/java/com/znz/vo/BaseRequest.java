package com.znz.vo;

import lombok.Data;

/**
 * Created by Administrator on 2017/5/13.
 */
@Data
public class BaseRequest<T> {

    private String token;

    private String imei;

    private String sid;

    private T data;


}
