package com.wushiyii.aop;

import com.wushiyii.annotation.aop.Aspect;
import com.wushiyii.annotation.aop.Order;
import com.wushiyii.aop.advice.AroundAdvice;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author wgq
 * @date 2020/3/29 10:33 下午
 */
@Slf4j
@Aspect(pointcut = "execution(* com.wushiyii.controller.TestController.returnFun(..))")
@Order(1)
public class TestAspect2 implements AroundAdvice {
    @Override
    public void after(Class<?> clazz, Method method, Object[] args, Object returnVal) {
        log.info("testAspect2 after method, class:{}, method:{}. args:{}, returnVal:{}", clazz.getName(), method.getName(), args, returnVal);
    }

    @Override
    public void before(Class<?> clazz, Method method, Object[] args) throws Throwable {
        log.info("testAspect2 before method, class:{}, method:{}. args:{}", clazz.getName(), method.getName(), args);
    }

    @Override
    public void throwing(Class<?> clazz, Method method, Object[] args, Throwable throwable) {
        log.info("testAspect2 method throws exception, class:{}, method:{}. args:{}, throwable:{}", clazz.getName(), method.getName(), args, throwable);
    }
}
