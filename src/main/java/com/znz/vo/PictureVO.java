package com.znz.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/9.
 */
@Data
public class PictureVO {
    private Long id;

    private String name;

    private String filePath;

    private Integer clickTimes;

    private Integer downloadTimes;

    private Date createTime;

    private String createUser;

}
