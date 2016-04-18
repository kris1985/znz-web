package com.znz.model;

import java.util.Date;

public class TTravelLine {
    private Integer id;

    private Integer attractionsId;

    private String spzx;

    private String days;

    private String jtfs;

    private Integer hotel;

    private String remark;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAttractionsId() {
        return attractionsId;
    }

    public void setAttractionsId(Integer attractionsId) {
        this.attractionsId = attractionsId;
    }

    public String getSpzx() {
        return spzx;
    }

    public void setSpzx(String spzx) {
        this.spzx = spzx == null ? null : spzx.trim();
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days == null ? null : days.trim();
    }

    public String getJtfs() {
        return jtfs;
    }

    public void setJtfs(String jtfs) {
        this.jtfs = jtfs == null ? null : jtfs.trim();
    }

    public Integer getHotel() {
        return hotel;
    }

    public void setHotel(Integer hotel) {
        this.hotel = hotel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}