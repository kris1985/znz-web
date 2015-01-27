package com.znz.listener;

import com.znz.model.User;
import com.znz.util.Constants;
import com.znz.vo.UserSession;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/1/26.
 */
@Slf4j
public class MySessionLister implements HttpSessionListener {

    private static Map onlineSessionMap = new HashMap();// 已登录的会话列表

    private static int activeSessions = 0;// 在线人数

    public MySessionLister() {

    }

    // 会话创建后执行方法
    public void sessionCreated(HttpSessionEvent hse) {
        // 将在线人数加一
        activeSessions++;

    }

    // 会话销毁执行方法
    public void sessionDestroyed(HttpSessionEvent hse) {
        // 获取即将删除的会话
        HttpSession session = hse.getSession();
        // 从在线会话map中根据登录用户ID移除会话,并让此会话失效。
        // 删除用户会话
        onlineSessionMap.remove(hse.getSession().getId());
        session.removeAttribute(Constants.USER_SESSION);
        session.invalidate();
        session = null;
        if (activeSessions > 0) {
            activeSessions--;
        }
    }

    /**
     * 判断用户是否已经登录，如果已经登录则作出替换操作,替换成功返回true
     *
     * @param newSession
     * @param oldSessionId
     * @return
     */
    public static boolean replaceSession(HttpSession newSession, String oldSessionId) {
        boolean flag = false;
        if (onlineSessionMap.containsKey(oldSessionId)) {
            // 根据用户ID在sessionMap中查找会话，如果存在并且不是同一个会话ID,则进入
            if (!isSameSession(newSession, oldSessionId)) {
                onlineSessionMap.remove(oldSessionId);
                // 保存刚创建的会话。
                System.out.println("old:"+oldSessionId);
                System.out.println("new:"+newSession.getId());
                onlineSessionMap.put(newSession.getId(), newSession);
                flag = true;
            }
        } else {
            // 如果会话列表中不包含该会话,则添加
            onlineSessionMap.put(oldSessionId, newSession);
        }
        return flag;
    }

    /**
     * 比较新会话和旧会话的ID，如果ID相同，则表示是同一个用户,返回true
     *
     * @param newSession
     * @param oldSessionId
     * @return boolean ID相同返回true,
     */
    public static boolean isSameSession(HttpSession newSession, String oldSessionId) {
        boolean flag = false;
        // 获得旧会话
        HttpSession oldSession = (HttpSession) onlineSessionMap.get(oldSessionId);
        if (oldSession != null && newSession.getId().equals(oldSession.getId())) {
            // 比较新会话和旧会话的ID，如果ID相同，则表示是同一个用户
            flag = true;
        }
        return flag;
    }



    public static int getActiveSessions() {
        return activeSessions;
    }

    public static void removeSession(String id) {
        onlineSessionMap.remove(id);
    }
}
