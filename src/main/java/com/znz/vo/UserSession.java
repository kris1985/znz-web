package com.znz.vo;

import com.znz.model.User;
import com.znz.model.UserAuth;
import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2015/1/27.
 */

public class UserSession {

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserAuths(List<UserAuth> userAuths) {
        this.userAuths = userAuths;
    }

    public User getUser() {
        return user;
    }

    public List<UserAuth> getUserAuths() {
        return userAuths;
    }

    private User user;

    private List<UserAuth>  userAuths;
}
