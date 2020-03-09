package com.wushiyii.core;

import com.wushiyii.annotation.mvc.Component;
import com.wushiyii.annotation.mvc.Controller;
import com.wushiyii.annotation.mvc.Repository;
import com.wushiyii.annotation.mvc.Service;
import com.wushiyii.util.ClassUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wgq
 * @date 2020/3/6 10:55 上午
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {

    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    private boolean isLoadedBean = false;

    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION =
            Arrays.asList(Component.class, Controller.class, Repository.class, Service.class);

    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    public Object addBean(Class<?> clazz, Object bean) {
        return beanMap.put(clazz, bean);
    }

    public void removeBean(Class<?> clazz) {
        beanMap.remove(clazz);
    }

    public Integer size() {
        return beanMap.size();
    }

    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        return beanMap.keySet()
                .stream()
                .filter(a -> a.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }


    public Set<Class<?>> getClassesBySuper(Class<?> superClass) {
        return beanMap.keySet()
                .stream()
                .filter(superClass::isAssignableFrom)
                .filter(clazz -> !clazz.equals(superClass))
                .collect(Collectors.toSet());
    }

    public void loadBeans(String basePackagePath) {
        if (isLoadedBean) {
            return;
        }
        Set<Class<?>> classes = ClassUtil.getPackageClass(basePackagePath);
        classes.stream().filter(
                clazz -> {
                    for (Class<? extends Annotation> annotationClass : BEAN_ANNOTATION) {
                        if (clazz.isAnnotationPresent(annotationClass)) {
                            return true;
                        }
                    }
                    return false;
                }
        ).forEach(clazz -> beanMap.put(clazz, ClassUtil.newInstance(clazz)));
        isLoadedBean = true;
    }

    public Object getClassInstance(Class<?> clz) {
        return Optional.ofNullable(beanMap.get(clz)).orElseGet(() -> getBean(getImplementClass(clz)));
    }


    private Class<?> getImplementClass(Class<?> interfaceClass) {
        return getClassesBySuper(interfaceClass)
                .stream().findFirst().orElse(null);
    }

    public boolean isLoadedBean() {
        return isLoadedBean;
    }

    public static BeanContainer getInstance() {
        return ContainerHolder.HOLDER.instance;
    }

    private enum ContainerHolder {
        HOLDER;
        private BeanContainer instance;

        ContainerHolder() {
            instance = new BeanContainer();
        }
    }
}
