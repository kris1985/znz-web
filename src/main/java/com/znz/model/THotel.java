package com.znz.model;

import java.util.Date;

public class THotel {
    private String id;

    private String hotelName;

    private String areaName;

    private String xj;

    private Integer dj;

    private Integer wj;

    private Integer hjz;

    private Integer cj;

    private Integer wy;

    private String zyzc;

    private String cgfs;

    private String cgdh;

    private String bz;

    private Integer ext1;

    private Integer ext2;

    private Integer ext3;

    private String ext4;

    private String ext5;

    private String ext6;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName == null ? null : hotelName.trim();
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getXj() {
        return xj;
    }

    public void setXj(String xj) {
        this.xj = xj == null ? null : xj.trim();
    }

    public Integer getDj() {
        return dj;
    }

    public void setDj(Integer dj) {
        this.dj = dj;
    }

    public Integer getWj() {
        return wj;
    }

    public void setWj(Integer wj) {
        this.wj = wj;
    }

    public Integer getHjz() {
        return hjz;
    }

    public void setHjz(Integer hjz) {
        this.hjz = hjz;
    }

    public Integer getCj() {
        return cj;
    }

    public void setCj(Integer cj) {
        this.cj = cj;
    }

    public Integer getWy() {
        return wy;
    }

    public void setWy(Integer wy) {
        this.wy = wy;
    }

    public String getZyzc() {
        return zyzc;
    }

    public void setZyzc(String zyzc) {
        this.zyzc = zyzc == null ? null : zyzc.trim();
    }

    public String getCgfs() {
        return cgfs;
    }

    public void setCgfs(String cgfs) {
        this.cgfs = cgfs == null ? null : cgfs.trim();
    }

    public String getCgdh() {
        return cgdh;
    }

    public void setCgdh(String cgdh) {
        this.cgdh = cgdh == null ? null : cgdh.trim();
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
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