package com.znz.filter;

import com.znz.listener.MySessionLister;
import com.znz.util.Constants;
import com.znz.vo.UserSession;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by Administrator on 2015/1/27.
 */
public  class LoginFilter implements Filter {
    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)  throws IOException, ServletException {
        HttpServletRequest request2 = (HttpServletRequest)request;
        HttpServletResponse response2 = (HttpServletResponse)response;
        HttpSession session = request2.getSession();
        Object obj = session.getAttribute(Constants.USER_SESSION);
        if(obj == null){
            redirect(request2,response2,"8888");
        }else{
            UserSession userSession = (UserSession)session.getAttribute(Constants.USER_SESSION);
            if(userSession.getUser().getAccessFlag()==0){
                //如果没有限制访问
                chain.doFilter(request, response);
            }else{
                String oldSessionId = userSession.getUser().getSessionId();
                boolean bool = MySessionLister.isSameSession(session, oldSessionId);
                if(bool){
                    // 如果是同一个session ID,则通过
                    chain.doFilter(request, response);
                }else{
                    MySessionLister.removeSession(session.getId());
                    session.removeAttribute(Constants.USER_SESSION);
                    session.invalidate();
                    redirect(request2,response2,"9999");
                }
            }

        }
    }



    private void redirect(HttpServletRequest request2, HttpServletResponse response2, String errorCode) throws IOException {


           // response2.sendRedirect(request2.getContextPath()+"/error?errorCode="+errorCode);
        response2.sendRedirect( Constants.INDEX_PAGE+"error?errorCode="+errorCode);

    }


    public void destroy() {
        this.filterConfig = null;
    }
}
