package com.wushiyii.aop.creator;

import com.wushiyii.aop.advisor.ProxyAdvisor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * 代理类创建器
 * @author wgq
 * @date 2020/3/20 1:25 下午
 */
public class ProxyCreator {

    public static Object createProxyInstance(Class<?> targetClazz, ProxyAdvisor advisor) {
        return Enhancer.create(targetClazz,
                (MethodInterceptor) (target, method, args, methodProxy)
                        -> advisor.doProxy(target, targetClazz, method, args, methodProxy));
    }

}
