package com.znz.vo;

import com.znz.model.User;
import com.znz.model.UserAuth;
import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2015/1/27.
 */

@Data
public class UserSession {

    private User user;

    private List<UserAuth>  userAuths;
}
