package com.znz.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by Administrator on 2015/1/25.
 */

public class UserAddVO {

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLimitIpFlag(int limitIpFlag) {
        this.limitIpFlag = limitIpFlag;
    }

    public void setLimitIps(String limitIps) {
        this.limitIps = limitIps;
    }

    public void setAccessFlag(int accessFlag) {
        this.accessFlag = accessFlag;
    }

    public void setMaxDownloadTimes(int maxDownloadTimes) {
        this.maxDownloadTimes = maxDownloadTimes;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAuths(List<String> auths) {
        this.auths = auths;
    }

    private String userName;

    private String pwd;

    private String company;

    private int limitIpFlag;

    private String limitIps;

    private int accessFlag;

    private int maxDownloadTimes;

    private String phone;

    private int userId;

    private List<String> auths;

    public String getUserName() {
        return userName;
    }

    public String getPwd() {
        return pwd;
    }

    public String getCompany() {
        return company;
    }

    public int getLimitIpFlag() {
        return limitIpFlag;
    }

    public String getLimitIps() {
        return limitIps;
    }

    public int getAccessFlag() {
        return accessFlag;
    }

    public int getMaxDownloadTimes() {
        return maxDownloadTimes;
    }

    public String getPhone() {
        return phone;
    }

    public int getUserId() {
        return userId;
    }

    public List<String> getAuths() {
        return auths;
    }
}
