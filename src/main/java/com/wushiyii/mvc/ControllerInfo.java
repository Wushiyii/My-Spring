package com.wushiyii.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author wgq
 * @date 2020/3/29 11:14 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerInfo {

    /**
     * controller class
     */
    private Class<?> controllerClass;

    /**
     * 执行方法
     */
    private Method invokeMethod;

    /**
     * 参数类型
     */
    private Map<String, Class<?>> methodParameter;


}
