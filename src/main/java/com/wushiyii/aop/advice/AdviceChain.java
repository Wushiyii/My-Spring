package com.wushiyii.aop.advice;

import com.wushiyii.aop.advisor.ProxyAdvisor;
import lombok.Getter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author wgq
 * @date 2020/3/29 9:33 下午
 */
@Getter
public class AdviceChain {

    /**
     * 目标类
     */
    private final Class<?> targetClass;

    /**
     * 目标实例
     */
    private final Object targetObject;

    /**
     * 目标方法
     */
    private final Method method;

    /**
     * 目标方法参数
     */
    private final Object[] args;


    /**
     * 代理方法
     */
    private MethodProxy methodProxy;

    /**
     * 代理通知队列
     */
    private List<ProxyAdvisor> proxyList;

    /**
     * 代理通知队列索引
     */
    private int index;

    public AdviceChain(Class<?> targetClass, Object targetObject, Method method, Object[] args, MethodProxy methodProxy, List<ProxyAdvisor> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
        this.proxyList = proxyList;
    }


    /**
     * 实现先入后出顺序调用被代理方法
     * @return 方法返回
     */
    public Object doAdviceChain() throws Throwable {
        Object returnVal;
        while (index < proxyList.size() && !proxyList.get(index).getPointcut().matches(method)) {
            index++;
        }
        if (index < proxyList.size()) {
            returnVal = proxyList.get(index++).doProxy(this);
        } else {
            returnVal = methodProxy.invokeSuper(targetObject, args);
        }
        return returnVal;
    }

}
