package com.wushiyii.ioc;

import com.wushiyii.annotation.ioc.Autowired;
import com.wushiyii.core.BeanContainer;
import com.wushiyii.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author wgq
 * @date 2020/3/9 3:50 下午
 */

public class IocHandler {

    private BeanContainer beanContainer;

    private Integer a = 0;

    public IocHandler() {
        beanContainer= BeanContainer.getInstance();
    }

    public void init() {
        for (Class<?> clazz : beanContainer.getClasses()) {
            Object bean = beanContainer.getBean(clazz);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class<?> fieldType = field.getType();
                    Object valueObject = beanContainer.getClassInstance(fieldType);
                    if (Objects.nonNull(valueObject)) {
                        ClassUtil.setField(bean, field, valueObject);
                    } else {
                        throw new RuntimeException("could not inject class, actual field name :" + fieldType.getName());
                    }
                }
            }
        }
    }
}
