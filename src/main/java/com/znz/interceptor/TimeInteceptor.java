package com.znz.interceptor;

import com.sun.tools.javac.code.Attribute.Constant;
import com.znz.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/6/3.
 */
@Slf4j
public class TimeInteceptor implements HandlerInterceptor {


    //before the actual handler will be executed
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        MDC.put("traceId", UUID.randomUUID().toString());
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        if (handler instanceof HandlerMethod) {
            StringBuilder sb = new StringBuilder(1000);
            HandlerMethod h = (HandlerMethod) handler;
            sb.append("Controller: ").append(h.getBean().getClass().getName()).append(",");
            sb.append("Method    : ").append(h.getMethod().getName()).append(",");
            sb.append("Params    : ").append(getParamString(request.getParameterMap())).append(":");
            sb.append("URI       : ").append(request.getRequestURI()).append("\n");
            log.info(sb.toString());
        }
        if(request.getRequestURI().indexOf("/admin")!=-1 && request.getSession().getAttribute(Constants.USER_SESSION) == null){
            redirect(response,"8888");
        }
        return true;
    }

    //after the handler is executed
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView)
            throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        if (handler instanceof HandlerMethod) {
            StringBuilder sb = new StringBuilder(1000);
            sb.append("CostTime:").append(executeTime).append("ms").append("\n");
            log.info(sb.toString());
        }
    }

    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
       // log.info(arg2.toString());
        if(arg3!=null){
            log.error(arg3.getLocalizedMessage(),arg3);
        }
        // TODO Auto-generated method stub

    }

    private String getParamString(Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> e : map.entrySet()) {
            sb.append(e.getKey()).append("=");
            String[] value = e.getValue();
            if (value != null && value.length == 1) {
                sb.append(value[0]).append("\t");
            } else {
                sb.append(Arrays.toString(value)).append("\t");
            }
        }
        return sb.toString();
    }

    private void redirect( HttpServletResponse response, String errorCode) throws
        IOException {
        // response2.sendRedirect(request2.getContextPath()+"/error?errorCode="+errorCode);
        response.sendRedirect( Constants.INDEX_PAGE+"error?errorCode="+errorCode);

    }
}
