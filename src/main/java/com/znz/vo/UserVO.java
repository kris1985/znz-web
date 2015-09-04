package com.znz.vo;

import com.znz.model.User;
import org.joda.time.DateTime;

/**
 * Created by Administrator on 2015/9/5.
 */
public class UserVO extends User {

    private String lastLoginTimeStr;

    public void setLastLoginTimeStr(String lastLoginTimeStr) {
        this.lastLoginTimeStr = lastLoginTimeStr;
    }

    public String getLastLoginTimeStr() {
        if(getLastLoginTime()!=null){
            return new DateTime(getLastLoginTime().getTime()).toString("yyyy-MM-dd HH:mm");
        }
        return lastLoginTimeStr;
    }
}
