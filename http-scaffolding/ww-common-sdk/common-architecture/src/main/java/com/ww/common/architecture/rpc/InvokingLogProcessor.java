package com.ww.common.architecture.rpc;

import com.ww.common.architecture.log.AccessLogContext;
import com.ww.common.architecture.log.AccessLogStrategy;
import com.ww.common.architecture.log.AccessLogStrategySelector;
import com.ww.common.base.BusinessException;
import com.ww.common.base.JacksonHelper;
import com.ww.common.base.annotation.AccessLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

@Slf4j
@Aspect
@Component
public class InvokingLogProcessor {
    @Resource
    private AccessLogStrategySelector accessLogStrategySelector;
    private final Random random = new Random();

    @Around("process() && @annotation(accessLog)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, AccessLog accessLog) throws Throwable {
        long startTimePoint = System.currentTimeMillis();
        String key = startTimePoint + "-" + random.nextInt(1000000);

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();

        // accessLogContext
        AccessLogContext accessLogContext = new AccessLogContext();
        accessLogContext.setRequestURL(String.format("%s#%s", methodSignature.getDeclaringType().getName(), methodName));
        accessLogContext.setStartTimePoint(startTimePoint);
        accessLogContext.setStartLogMessage(String.format(
                "start(%s) to invoke method(%s); parameters: %s",
                key,
                methodName,
                JacksonHelper.toJson(proceedingJoinPoint.getArgs(), false)));

        // should log
        AccessLogStrategy accessLogStrategy = accessLogStrategySelector.select(accessLogContext.getRequestURL());
        accessLogContext.setShouldLog(null == accessLogStrategy || accessLogStrategy.shouldLog());
        if (accessLogContext.isShouldLog()) {
            log.info(accessLogContext.getStartLogMessage());
        }

        try {
            Object result = proceedingJoinPoint.proceed();

            long rt = System.currentTimeMillis() - startTimePoint;
            if (accessLogContext.isShouldLog()) {
                log.info("success({})-{} to invoke method({}); result({})",
                        key,
                        rt,
                        methodName,
                        result);
            }

            return result;
        } catch (BusinessException e) {
            if (!accessLogContext.isShouldLog()) {
                log.info(accessLogContext.getStartLogMessage());
            }

            long rt = System.currentTimeMillis() - startTimePoint;
            String message = String.format("failed(%s)-%d to invoke method(%s); parameters: %s",
                    key,
                    rt,
                    methodName,
                    JacksonHelper.toJson(proceedingJoinPoint.getArgs(), false));
            if (e.isErrorLogLevel()) {
                log.error(message, e);
            } else {
                log.warn(message, e);
            }

            throw e;
        } catch (Throwable throwable) {
            if (!accessLogContext.isShouldLog()) {
                log.info(accessLogContext.getStartLogMessage());
            }

            long rt = System.currentTimeMillis() - startTimePoint;
            log.error(String.format("failed(%s)-%d to invoke method(%s); parameters: %s",
                            key,
                            rt,
                            methodName,
                            JacksonHelper.toJson(proceedingJoinPoint.getArgs(), false)),
                    throwable);

            throw throwable;
        }
    }

    @Pointcut("@annotation(com.ww.common.base.annotation.AccessLog)")
    public void process() {
    }

    @Before(value = "process()")
    public void before() {
    }

    @After(value = "process()")
    public void after() {
    }
}
