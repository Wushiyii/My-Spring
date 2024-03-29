package com.wushiyii.aop;

import com.wushiyii.annotation.aop.Aspect;
import com.wushiyii.annotation.aop.Order;
import com.wushiyii.annotation.mvc.Controller;
import com.wushiyii.aop.advice.AroundAdvice;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@Aspect(pointcut = "execution(* com.wushiyii.controller.TestController.returnFun(..))")
@Order
public class TestAspect implements AroundAdvice {
    @Override
    public void after(Class<?> clazz, Method method, Object[] args, Object returnVal) {
        log.info("testAspect after method, class:{}, method:{}. args:{}, returnVal:{}", clazz.getName(), method.getName(), args, returnVal);
    }

    @Override
    public void before(Class<?> clazz, Method method, Object[] args) throws Throwable {
        log.info("testAspect before method, class:{}, method:{}. args:{}", clazz.getName(), method.getName(), args);
    }

    @Override
    public void throwing(Class<?> clazz, Method method, Object[] args, Throwable throwable) {
        log.info("testAspect method throws exception, class:{}, method:{}. args:{}, throwable:{}", clazz.getName(), method.getName(), args, throwable);
    }
}
