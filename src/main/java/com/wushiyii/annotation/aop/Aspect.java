package com.wushiyii.annotation.aop;

import java.lang.annotation.*;

/**
 * @author wgq
 * @date 2020/3/20 12:55 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    String pointcut() default "";

}
