package com.wushiyii.aop.advice;

import java.lang.reflect.Method;

/**
 * @author wgq
 * @date 2020/3/20 1:00 下午
 */
public interface MethodBeforeAdvice extends Advice {

    void before(Class<?> clazz, Method method, Object[] args) throws Throwable;

}
