package com.znz.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
@Data
public class UserInfo {
    private String token;
    private String userName;
    private boolean recommend;
}
