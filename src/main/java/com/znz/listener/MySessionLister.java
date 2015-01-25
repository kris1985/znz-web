package com.znz.listener;

import com.znz.model.User;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/1/26.
 */
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
        deleteSession(session);
        if (activeSessions > 0) {
            activeSessions--;
        }
    }

    /**
     * 判断用户是否已经登录，如果已经登录则作出替换操作,替换成功返回true
     *
     * @param newSession
     * @param kskh
     * @return
     */
    public static boolean replaceSession(HttpSession newSession, String kskh) {
        boolean flag = false;
        if (onlineSessionMap.containsKey(kskh)) {
            // 根据用户ID在sessionMap中查找会话，如果存在并且不是同一个会话ID,则进入
            if (!isSameSession(newSession, kskh)) {
                // 如果会话ID不相等,说明是两人同时登录
                HttpSession oldSession = (HttpSession) onlineSessionMap.get(kskh);
                try {
                    // 销毁之前的会话
                    deleteSession(oldSession);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // 保存刚创建的会话。
                onlineSessionMap.put(kskh, newSession);
                flag = true;
            } else {
                // 如果会话ID相等,说明是同一个人登录,不做任何操作
                flag = false;
            }
        } else {
            // 如果会话列表中不包含该会话,则添加
            onlineSessionMap.put(kskh, newSession);
            flag = false;
        }
        return flag;
    }

    /**
     * 比较新会话和旧会话的ID，如果ID相同，则表示是同一个用户,返回true
     *
     * @param newSession
     * @param kskh
     * @return boolean ID相同返回true,
     */
    public static boolean isSameSession(HttpSession newSession, String kskh) {
        boolean flag = false;
        HttpSession oldSession = null;
        try {
            // 获得旧会话
            oldSession = (HttpSession) onlineSessionMap.get(kskh);
        } catch (Exception e) {
        }
        if (oldSession != null) {
            // 比较新会话和旧会话的ID，如果ID相同，则表示是同一个用户
            if (newSession.getId().equals(oldSession.getId())) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 从在线会话map中移除会话。
     *
     * @param session
     * @return
     */
    public static boolean deleteSession(HttpSession session) {
        if (session != null) {
            try {
                User usersession = (User) session.getAttribute("user");
                if (usersession != null) {
                    String sessionId  = usersession.getSessionId();
                    if (sessionId != null) {
                        // 移除用户会话
                        onlineSessionMap.remove(sessionId);
                    }
                    // 删除用户会话
                    session.removeAttribute("userSession");
                    session.invalidate();
                    session = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static int getActiveSessions() {
        return activeSessions;
    }
}
