package com.wushiyii.aop.advisor;

import com.wushiyii.aop.advice.Advice;
import com.wushiyii.aop.advice.AfterReturningAdvice;
import com.wushiyii.aop.advice.MethodBeforeAdvice;
import com.wushiyii.aop.advice.ThrowableAdvice;
import com.wushiyii.aop.pointcut.ProxyPointcut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author wgq
 * @date 2020/3/20 1:10 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyAdvisor {

    private Advice advice;

    private ProxyPointcut pointcut;

    public Object doProxy(Object target, Class<?> targetClazz, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        //判断是否匹配切面方法，不匹配直接执行
        if (!pointcut.matches(method)) {
            return proxy.invokeSuper(target, args);
        }
        if (advice instanceof MethodBeforeAdvice) {
            ((MethodBeforeAdvice) advice).before(targetClazz, method, args);
        }
        Object returnVal = null;
        try {
            returnVal = proxy.invokeSuper(target, args);
            if (advice instanceof AfterReturningAdvice) {
                ((AfterReturningAdvice) advice).after(targetClazz, method, args, returnVal);
            }
        } catch (Exception e) {
            if (advice instanceof ThrowableAdvice) {
                ((ThrowableAdvice) advice).throwing(targetClazz, method, args, e);
            } else {
                throw e;
            }
        }
        return returnVal;
    }
}
