package com.znz.vo;

import com.znz.model.User;
import com.znz.model.UserAuth;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2015/1/27
 */

public class UserSession implements Serializable{

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
