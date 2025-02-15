package com.ww.common.architecture.login;

import com.ww.common.base.annotation.AccessLoginStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class LoginStatusProcessor {
    @Value("${loginStatusProcessor.log.Switch: true}")
    private Boolean logSwitch;

    @Resource
    private LoginStatusGetterSelector loginStatusGetterSelector;

    @Around("process() && @annotation(accessLoginStatus)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, AccessLoginStatus accessLoginStatus) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String requestUrl = String.format("%s#%s", methodSignature.getDeclaringType().getName(), methodSignature.getMethod().getName());

        HttpServletRequest request = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            request = servletRequestAttributes.getRequest();
        }
        Assert.notNull(request, "HttpServletRequest is null. please check if you add AccessLoginStatus annotation on your controller method. requestUrl: " + requestUrl);

        // get loginStatus
        AbstractLoginStatusGetter loginStatusGetter = loginStatusGetterSelector.select(accessLoginStatus.mode());
        LoginStatus loginStatus = loginStatusGetter.getLoginStatus(request);
        if(logSwitch){
            log.info("get loginStatus({}) on controller method({})", loginStatus, requestUrl);
        }

        // set loginStatus to parameters
        loginStatusGetter.setLoginStatus(proceedingJoinPoint.getArgs(), loginStatus);

        try {
            Object result = proceedingJoinPoint.proceed();

            return result;
        } catch (Throwable throwable) {

            throw throwable;
        }
    }

    @Pointcut("@annotation(com.ww.common.base.annotation.AccessLoginStatus)")
    public void process() {
    }

    @Before(value = "process()")
    public void before() {
    }

    @After(value = "process()")
    public void after() {
    }
}
