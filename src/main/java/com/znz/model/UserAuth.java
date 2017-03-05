package com.znz.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserAuth {
    private Integer id;

    private Integer userId;

    private String authId;

    private Date createTime;

}