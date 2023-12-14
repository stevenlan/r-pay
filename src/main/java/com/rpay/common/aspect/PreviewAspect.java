package com.rpay.common.aspect;

import com.rpay.common.exception.BusinessException;
import com.rpay.common.utils.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 演示环境拦截器
 *
 * @author dinghao
 * @date 2021/3/16
 */
@Slf4j
@Aspect
@Component
public class PreviewAspect {

    @Value("${fs.isPreview}")
    private boolean isPreview = false;

    @Pointcut("execution(* com.rpay.controller.*.*(..)) && !execution(* com.rpay.controller.LoginController.login(..))")
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        if (StringUtils.equalsIgnoreCase(request.getMethod(), HttpMethod.POST.name()) && isPreview) {
            log.error("演示环境不能操作！");
            throw new BusinessException("演示环境下不能操作！");
        }
        return point.proceed();
    }

}
