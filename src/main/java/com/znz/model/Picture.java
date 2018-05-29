package com.znz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
public class Picture implements Serializable {
    private Long id;

    private String name;

    private String filePath;

    private Integer clickTimes;

    private Integer downloadTimes;

    private Date createTime;

    private String createUser;

    private String attach;

    private Integer secCategory;

    private String recId;

    private String gid;

    private String width;

    private String height;

    private String size;

    private String sort;

    public static void main(String[] args) {
        String originalName = "品牌_Nike_2018年05月18日_0132_13_17.jpg";
        System.out.println(originalName.substring(originalName.indexOf("_",4)+1));
    }


}