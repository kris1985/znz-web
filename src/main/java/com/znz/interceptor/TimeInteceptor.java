package com.znz.interceptor;

import com.sun.tools.javac.code.Attribute.Constant;
import com.znz.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;

/**
 * Created by Administrator on 2017/6/3.
 */
@Slf4j
public class TimeInteceptor implements HandlerInterceptor  {


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        String ip = getIp(request);
        MDC.put("traceId", UUID.randomUUID().toString());
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        if (handler instanceof HandlerMethod) {
            StringBuilder sb = new StringBuilder(1000);
            HandlerMethod h = (HandlerMethod) handler;
            sb.append("Controller: ").append(h.getBean().getClass().getName()).append(",");
            sb.append("Method    : ").append(h.getMethod().getName()).append(",");
            sb.append("Params    : ").append(getParamString(request.getParameterMap())).append(":");
            sb.append("URI       : ").append(request.getRequestURI()).append("  ip:").append(ip);
            log.info(sb.toString());
        }
        if(request.getRequestURI().indexOf("/admin")!=-1 && request.getSession().getAttribute(Constants.USER_SESSION) == null){
            redirect(response,"8888");
        }
        return true;
    }

    private String getIp(HttpServletRequest request) {
        String ip  =  request.getHeader( " x-forwarded-for " );
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getHeader( " Proxy-Client-IP " );
        }
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getHeader( " WL-Proxy-Client-IP " );
        }
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView)
            throws Exception {

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        if (handler instanceof HandlerMethod) {
            StringBuilder sb = new StringBuilder(1000);
            sb.append("CostTime:").append(executeTime).append("ms");
            log.info(sb.toString());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0,
                                HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
       // log.info(arg2.toString());
        if(arg3!=null){
            log.error(arg3.getLocalizedMessage(),arg3);
        }
        // TODO Auto-generated method stub

    }

   /* @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        log.info("reponse args[{}]",formatArgs(args));
        log.info("method:"+method);
    }*/

    private String formatArgs(Object[] args){
        StringBuffer argsStr = new StringBuffer();
        for(Object arg:args){
            if(arg!=null){
                if(HttpServletRequest.class.isAssignableFrom(arg.getClass())||HttpServletResponse.class.isAssignableFrom(arg.getClass())){
                    continue;
                }else{
                    argsStr.append(JSON.toJSONString(arg)).append(",");
                }
            }else{
                argsStr.append("null,");
            }
        }
        if(argsStr.length()>0){
            argsStr.setLength(argsStr.length()-1);
        }
        return argsStr.toString();
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
