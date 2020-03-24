package com.wushiyii.aop.advisor;

import com.wushiyii.aop.advice.Advice;
import com.wushiyii.aop.advice.AfterReturningAdvice;
import com.wushiyii.aop.advice.MethodBeforeAdvice;
import com.wushiyii.aop.advice.ThrowableAdvice;
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

    public Object doProxy(Object target, Class<?> targetClazz, Method method, Object[] args, MethodProxy proxy) throws Throwable {
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

    public ProxyAdvisor(Advice advice) {
        this.advice = advice;
    }
}
