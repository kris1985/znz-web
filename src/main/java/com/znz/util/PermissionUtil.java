package com.znz.util;

import com.znz.vo.UserSession;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by admin on 2016/4/18.
 */
public class PermissionUtil {

    public static boolean checkPermisson(HttpServletRequest request) {
        UserSession userSession = (UserSession) request.getSession().getAttribute(
                Constants.USER_SESSION);
        if (userSession.getUser().getUserType() != 2 && userSession.getUser().getUserType() != 3 &&  userSession.getUser().getUserType() != 0) {
            return false;
        }
        return true;
    }
}
