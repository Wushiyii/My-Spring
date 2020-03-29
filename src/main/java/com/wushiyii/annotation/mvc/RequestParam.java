package com.wushiyii.annotation.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wgq
 * @date 2020/3/29 11:05 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {

    /**
     * 参数别名
     */
    String value() default "";

    /**
     * 是否必传
     */
    boolean required() default true;
}
