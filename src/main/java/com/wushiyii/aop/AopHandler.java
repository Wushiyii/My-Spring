package com.wushiyii.aop;

import com.wushiyii.annotation.aop.Aspect;
import com.wushiyii.aop.advice.Advice;
import com.wushiyii.aop.advisor.ProxyAdvisor;
import com.wushiyii.aop.creator.ProxyCreator;
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
                .forEach(clazz -> {
                    Advice advice = (Advice) beanContainer.getBean(clazz);
                    Aspect aspect = clazz.getAnnotation(Aspect.class);
                    beanContainer.getClassesByAnnotation(aspect.target()).stream()
                            .filter(aspectClazz -> !Advice.class.isAssignableFrom(aspectClazz))
                            .filter(aspectClazz -> !aspectClazz.isAnnotationPresent(Aspect.class))
                            .forEach(aspectClazz -> {
                                ProxyAdvisor advisor = new ProxyAdvisor(advice);
                                Object proxyInstance = ProxyCreator.createProxyInstance(aspectClazz, advisor);
                                beanContainer.addBean(aspectClazz, proxyInstance);
                            });
                });
    }
}
