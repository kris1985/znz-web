package com.znz.model;

import java.util.Date;
import java.util.List;

public class Picture {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public Integer getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(Integer clickTimes) {
        this.clickTimes = clickTimes;
    }

    public Integer getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(Integer downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getAttach() {
        return attach;
    }

    public Integer getSecCategory() {
        return secCategory;
    }

    public void setSecCategory(Integer secCategory) {
        this.secCategory = secCategory;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }
}