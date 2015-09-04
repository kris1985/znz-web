package com.znz.model;

import java.util.Date;

public class User {
    private Integer userId;

    private String userName;

    private String pwd;

    private String company;

    private Integer limitIpFlag;

    private String limitIps;

    private Integer accessFlag;

    private Integer maxDownloadTimes;

    private Integer downloadPerDay;

    private Integer downloadTotal;

    private String phone;

    private Date createTime;

    private Date updateTime;

    private String sessionId;

    private Integer userType;

    private Date lastLoginTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public Integer getLimitIpFlag() {
        return limitIpFlag;
    }

    public void setLimitIpFlag(Integer limitIpFlag) {
        this.limitIpFlag = limitIpFlag;
    }

    public String getLimitIps() {
        return limitIps;
    }

    public void setLimitIps(String limitIps) {
        this.limitIps = limitIps == null ? null : limitIps.trim();
    }

    public Integer getAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(Integer accessFlag) {
        this.accessFlag = accessFlag;
    }

    public Integer getMaxDownloadTimes() {
        return maxDownloadTimes;
    }

    public void setMaxDownloadTimes(Integer maxDownloadTimes) {
        this.maxDownloadTimes = maxDownloadTimes;
    }

    public Integer getDownloadPerDay() {
        return downloadPerDay;
    }

    public void setDownloadPerDay(Integer downloadPerDay) {
        this.downloadPerDay = downloadPerDay;
    }

    public Integer getDownloadTotal() {
        return downloadTotal;
    }

    public void setDownloadTotal(Integer downloadTotal) {
        this.downloadTotal = downloadTotal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }
}