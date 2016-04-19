package com.znz.model;

import java.util.Date;

public class TLinePrice {
    private String id;

    private String lineId;

    private Integer stay;

    private Integer hotelDiff;

    private Integer pickUp;

    private Integer presented;

    private Integer spzxPrice;

    private Integer childrenPrice;

    private Integer profit;

    private Integer royalty;

    private Integer finalPrice;

    private String remark;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public Integer getStay() {
        return stay;
    }

    public void setStay(Integer stay) {
        this.stay = stay;
    }

    public Integer getHotelDiff() {
        return hotelDiff;
    }

    public void setHotelDiff(Integer hotelDiff) {
        this.hotelDiff = hotelDiff;
    }

    public Integer getPickUp() {
        return pickUp;
    }

    public void setPickUp(Integer pickUp) {
        this.pickUp = pickUp;
    }

    public Integer getPresented() {
        return presented;
    }

    public void setPresented(Integer presented) {
        this.presented = presented;
    }

    public Integer getSpzxPrice() {
        return spzxPrice;
    }

    public void setSpzxPrice(Integer spzxPrice) {
        this.spzxPrice = spzxPrice;
    }

    public Integer getChildrenPrice() {
        return childrenPrice;
    }

    public void setChildrenPrice(Integer childrenPrice) {
        this.childrenPrice = childrenPrice;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public Integer getRoyalty() {
        return royalty;
    }

    public void setRoyalty(Integer royalty) {
        this.royalty = royalty;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
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