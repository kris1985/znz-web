package com.znz.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by huangtao on 2015/1/27.
 */
@Data
public class FileNodeVO {
    private String path; //文件绝对路径
    private String url; //图片显示路径
    private String thumbUrl; //图片显示路径
    private String name;
    private long lastModified;
    private boolean directory;
}
