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

    private Integer ext1;

    private Integer ext2;

    private Integer ext3;

    private String ext4;

    private String ext5;

    private String ext6;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId == null ? null : lineId.trim();
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

    public Integer getExt1() {
        return ext1;
    }

    public void setExt1(Integer ext1) {
        this.ext1 = ext1;
    }

    public Integer getExt2() {
        return ext2;
    }

    public void setExt2(Integer ext2) {
        this.ext2 = ext2;
    }

    public Integer getExt3() {
        return ext3;
    }

    public void setExt3(Integer ext3) {
        this.ext3 = ext3;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4 == null ? null : ext4.trim();
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5 == null ? null : ext5.trim();
    }

    public String getExt6() {
        return ext6;
    }

    public void setExt6(String ext6) {
        this.ext6 = ext6 == null ? null : ext6.trim();
    }
}