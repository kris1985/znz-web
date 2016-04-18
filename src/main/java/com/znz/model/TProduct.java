package com.znz.model;

import java.util.Date;

public class TProduct {
    private Integer id;

    private String prodName;

    private String starting;

    private String destination;

    private Integer prodSort;

    private Integer prodSale;

    private Date createTime;

    private Date updateTime;

    private String remark;

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

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting == null ? null : starting.trim();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination == null ? null : destination.trim();
    }

    public Integer getProdSort() {
        return prodSort;
    }

    public void setProdSort(Integer prodSort) {
        this.prodSort = prodSort;
    }

    public Integer getProdSale() {
        return prodSale;
    }

    public void setProdSale(Integer prodSale) {
        this.prodSale = prodSale;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}