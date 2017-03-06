package com.znz.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
public class User {
    private Integer userId;

    private String userName;

    private String pwd;

    private String company;

    private Integer limitIpFlag;

    private String limitIps;

    private Integer accessFlag;

    private Integer maxDownloadTimes;

    private Integer downloadPerDay;

    private Integer downloadTotal;

    private String phone;

    private Date createTime;

    private Date updateTime;

    private String sessionId;

    private Integer userType;

    private Date lastLoginTime;

    private String watermark;

    private Integer recommendFlag;

    private String device;



}