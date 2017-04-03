package com.znz.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/3.
 */
public class PicRecommend {

    private Long id;

    private Long pictureId;

    private Integer userId;

    private Integer partionCode;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPartionCode() {
        return partionCode;
    }

    public void setPartionCode(Integer partionCode) {
        this.partionCode = partionCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
