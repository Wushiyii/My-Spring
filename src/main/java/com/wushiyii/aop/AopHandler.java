package com.wushiyii.aop;

import com.wushiyii.annotation.aop.Aspect;
import com.wushiyii.aop.advice.Advice;
import com.wushiyii.aop.advisor.ProxyAdvisor;
import com.wushiyii.aop.creator.ProxyCreator;
import com.wushiyii.aop.pointcut.ProxyPointcut;
import com.wushiyii.core.BeanContainer;

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
        beanContainer.getClassesBySuper(Advice.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(Aspect.class))
                .map(this::createProxyAdvisor)
                .forEach(advisor -> beanContainer.getClasses()
                        .stream()
                        .filter(targetClazz -> !Advice.class.isAssignableFrom(targetClazz))
                        .filter(targetClazz -> !targetClazz.isAnnotationPresent(Aspect.class))
                        .forEach(targetClazz -> {
                            if (advisor.getPointcut().matches(targetClazz)) {
                                Object proxyInstance = ProxyCreator.createProxyInstance(targetClazz, advisor);
                                beanContainer.addBean(targetClazz, proxyInstance);
                            }
                        })
                );
    }

    private ProxyAdvisor createProxyAdvisor(Class<?> aspectClazz) {
        String expression = aspectClazz.getAnnotation(Aspect.class).pointcut();
        ProxyPointcut pointcut = new ProxyPointcut();
        pointcut.setExpression(expression);
        Advice advice = (Advice) beanContainer.getBean(aspectClazz);
        return new ProxyAdvisor(advice, pointcut);
    }
}
