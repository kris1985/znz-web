package com.znz.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/6/3.
 */
@Aspect
@Component
@Slf4j
public class PerformanceInterceptor {

    //我需要监控所有controlller的耗时（controller放在包com.parry.demo.controller下面）
    @Around("execution(* com.znz.controller.*.*(..))")
    public Object logTome(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        String method = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().getName();
        Object ret = pjp.proceed();
        String time = String.valueOf(System.currentTimeMillis() - begin);
        StringBuilder s = new StringBuilder();
        s.append(className).append(".").append(method).append(",cost:").append(time);
        log.info(s.toString());
        return ret;
    }
}
