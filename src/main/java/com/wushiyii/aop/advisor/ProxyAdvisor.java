package com.wushiyii.aop.advisor;

import com.wushiyii.aop.advice.*;
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

    private int order;

    public Object doProxy(AdviceChain adviceChain) throws Throwable {
        Object returnVal = null;
        Class<?> targetClazz = adviceChain.getTargetClass();
        Object[] args = adviceChain.getArgs();
        Method method = adviceChain.getMethod();
        if (advice instanceof MethodBeforeAdvice) {
            ((MethodBeforeAdvice) advice).before(targetClazz, method, args);
        }
        try {
            returnVal = adviceChain.doAdviceChain();
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
