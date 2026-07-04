package com.mentalhealth.config;

import com.mentalhealth.entity.SysLog;
import com.mentalhealth.mapper.SysLogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Pointcut("execution(* com.mentalhealth.controller.*.*(..)) && !execution(* com.mentalhealth.controller.AuthController.*(..))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        long duration = System.currentTimeMillis() - start;

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                SysLog log = new SysLog();
                log.setUserId((Long) request.getAttribute("userId"));
                log.setUsername((String) request.getAttribute("username"));
                log.setOperation(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
                log.setMethod(request.getMethod() + " " + request.getRequestURI());
                log.setParams(JSON.toJSONString(point.getArgs()));
                log.setIp(request.getRemoteAddr());
                log.setDuration(duration);
                sysLogMapper.insert(log);
            }
        } catch (Exception ignored) {}
        return result;
    }
}

