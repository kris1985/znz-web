package com.znz.model;

import java.util.Date;

public class TAttractions {
    private Integer id;

    private String prodName;

    private String areaName;

    private String adultPrice;

    private String childrenPrice;

    private String disPrice;

    private String remark;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName == null ? null : prodName.trim();
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(String adultPrice) {
        this.adultPrice = adultPrice == null ? null : adultPrice.trim();
    }

    public String getChildrenPrice() {
        return childrenPrice;
    }

    public void setChildrenPrice(String childrenPrice) {
        this.childrenPrice = childrenPrice == null ? null : childrenPrice.trim();
    }

    public String getDisPrice() {
        return disPrice;
    }

    public void setDisPrice(String disPrice) {
        this.disPrice = disPrice == null ? null : disPrice.trim();
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