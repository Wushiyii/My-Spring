package com.wushiyii.aop.advice;

import java.lang.reflect.Method;

/**
 * @author wgq
 * @date 2020/3/20 1:02 下午
 */
public interface AfterReturningAdvice extends Advice {

    void after(Class<?> clazz, Method method, Object[] args, Object returnVal);

}
