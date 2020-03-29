package com.wushiyii.aop.creator;

import com.wushiyii.aop.advice.AdviceChain;
import com.wushiyii.aop.advisor.ProxyAdvisor;
import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理类创建器
 * @author wgq
 * @date 2020/3/20 1:25 下午
 */
public class ProxyCreator {

    public static Object createProxyInstance(Class<?> targetClazz, List<ProxyAdvisor> proxyList) {
        return Enhancer.create(targetClazz, new AdviceMethodInterceptor(targetClazz, proxyList));
    }

    @AllArgsConstructor
    private static class AdviceMethodInterceptor implements MethodInterceptor {

        private final Class<?> targetClass;

        private List<ProxyAdvisor> proxyAdvisorList;

        @Override
        public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            return new AdviceChain(targetClass, target, method, args, methodProxy, proxyAdvisorList).doAdviceChain();
        }
    }
}
