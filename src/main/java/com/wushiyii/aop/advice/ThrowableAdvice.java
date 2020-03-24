package com.wushiyii.aop.advice;

import java.lang.reflect.Method;

/**
 * @author wgq
 * @date 2020/3/20 1:05 下午
 */
public interface ThrowableAdvice extends Advice {

    void throwing(Class<?> clazz, Method method, Object[] args, Throwable throwable);

}
