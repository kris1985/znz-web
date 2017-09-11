package com.znz.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
@Data
@ToString
public class UserInfo {
    private String token;
    private String userName;
    private boolean recommend;
}
