package com.wushiyii.aop;

import com.wushiyii.annotation.aop.Aspect;
import com.wushiyii.annotation.aop.Order;
import com.wushiyii.aop.advice.Advice;
import com.wushiyii.aop.advisor.ProxyAdvisor;
import com.wushiyii.aop.creator.ProxyCreator;
import com.wushiyii.aop.pointcut.ProxyPointcut;
import com.wushiyii.core.BeanContainer;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wgq
 * @date 2020/3/20 1:17 下午
 */
public class AopHandler {

    private BeanContainer beanContainer;

    public AopHandler() {
        this.beanContainer = BeanContainer.getInstance();
    }

    public void init() {
        List<ProxyAdvisor> proxyList = beanContainer.getClassesBySuper(Advice.class).stream()
                .filter(clazz -> clazz.isAnnotationPresent(Aspect.class))
                .map(this::createProxyAdvisor)
                .collect(Collectors.toList());

        beanContainer.getClasses().stream()
                .filter(clazz -> !Advice.class.isAssignableFrom(clazz))
                .filter(clazz -> !clazz.isAnnotationPresent(Aspect.class))
                .forEach(clazz -> {
                    List<ProxyAdvisor> matchedProxyList = this.createMatchProxyList(proxyList, clazz);
                    if (matchedProxyList.size() > 0) {
                        Object proxyInstance = ProxyCreator.createProxyInstance(clazz, matchedProxyList);
                        beanContainer.addBean(clazz, proxyInstance);
                    }
                });
    }

    private ProxyAdvisor createProxyAdvisor(Class<?> aspectClazz) {
        int order = 0;
        if (aspectClazz.isAnnotationPresent(Order.class)) {
            order = aspectClazz.getAnnotation(Order.class).value();
        }
        String expression = aspectClazz.getAnnotation(Aspect.class).pointcut();
        ProxyPointcut pointcut = new ProxyPointcut();
        pointcut.setExpression(expression);
        Advice advice = (Advice) beanContainer.getBean(aspectClazz);
        return new ProxyAdvisor(advice, pointcut, order);
    }

    private List<ProxyAdvisor> createMatchProxyList(List<ProxyAdvisor> proxyAdvisors, Class<?> targetClass) {
        return proxyAdvisors.stream()
                .filter(a -> a.getPointcut().matches(targetClass))
                .sorted(Comparator.comparingInt(ProxyAdvisor::getOrder))
                .collect(Collectors.toList());
    }
}
