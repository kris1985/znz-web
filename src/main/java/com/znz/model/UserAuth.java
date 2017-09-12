package com.znz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserAuth implements Serializable {
    private Integer id;

    private Integer userId;

    private String authId;

    private Date createTime;

}