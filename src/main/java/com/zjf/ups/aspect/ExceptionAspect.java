package com.zjf.ups.aspect;

import com.zjf.ups.common.CommonException;
import com.zjf.ups.common.ExceptionEnum;
import com.zjf.ups.common.RestResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class ExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);

    /**
     * 异常处理切入点
     */
    @Pointcut("execution( * com.zjf.ups.controller.*.*(..))")
    public void exceptionPoint() {
    }

    @Around("exceptionPoint()")
    public Object handleException(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed(pjp.getArgs());
        } catch (Throwable t) {
            logger.error("ExceptionAspect error, method:{}, args:{}", pjp.getSignature().getName(), Arrays.toString(pjp.getArgs()));
            logger.error("ExceptionAspect error", t);
            return parseException(t);
        }
    }

    /**
     * 异常处理
     *
     * @param t
     * @return
     */
    private RestResult parseException(Throwable t) {
        if (t instanceof CommonException) {
            CommonException e = (CommonException) t;
            return new RestResult(e.getErrorCode(), e.getMessage());
        } else {
            Exception e = (Exception) t;
            return new RestResult(ExceptionEnum.THROWABLE_ERROR.getErrorCode(), e.getMessage());
        }
    }
}
