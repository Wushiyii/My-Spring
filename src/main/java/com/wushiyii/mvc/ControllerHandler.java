package com.wushiyii.mvc;

import com.wushiyii.annotation.mvc.RequestMapping;
import com.wushiyii.annotation.mvc.RequestMethod;
import com.wushiyii.annotation.mvc.RequestParam;
import com.wushiyii.core.BeanContainer;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Request;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wgq
 * @date 2020/3/29 11:17 下午
 */
@Slf4j
public class ControllerHandler {

    private Map<PathInfo, ControllerInfo> pathAndControllerMap = new ConcurrentHashMap<>();

    private BeanContainer beanContainer;

    public ControllerHandler() {
        beanContainer = BeanContainer.getInstance();
        Set<Class<?>> classesBySuper = beanContainer.getClassesBySuper(RequestMapping.class);
        for (Class<?> clazz : classesBySuper) {
            this.putPathAndControllerInfo(clazz);
        }
    }

    public ControllerInfo getControllerInfo(String requestMethod, String requestPath) {
        return pathAndControllerMap.get(new PathInfo(requestMethod, requestPath));
    }

    private void putPathAndControllerInfo(Class<?> clazz) {
        RequestMapping classRequestAnnotation = clazz.getAnnotation(RequestMapping.class);
        String classBasePath = classRequestAnnotation.value();
        // 遍历controller中的方法
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                //获取方法上的requestMapping注解
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                String methodPath = methodAnnotation.value();
                RequestMethod requestMethod = methodAnnotation.method();
                PathInfo pathInfo = new PathInfo(requestMethod.name(), classBasePath + methodPath);
                if (pathAndControllerMap.containsKey(pathInfo)) {
                    log.error("Path:[{}]重复注册", pathInfo.getRequestPath());
                    throw new RuntimeException("控制器Path重复注册");
                }
                //获取方法的参数名称及其对应类型
                Map<String, Class<?>> params = new HashMap<>();
                for (Parameter methodParam : method.getParameters()) {
                    if (methodParam.isAnnotationPresent(RequestParam.class)) {
                        RequestParam requestParam = methodParam.getAnnotation(RequestParam.class);
                        params.put(requestParam.value(), methodParam.getType());
                    } else {
                        throw new RuntimeException("必须有RequestParam指定的参数名");
                    }
                }
                //生成ControllerInfo并放入map中
                ControllerInfo controllerInfo = new ControllerInfo(clazz, method, params);
                pathAndControllerMap.put(pathInfo, controllerInfo);
                log.info("Add Controller RequestMethod:{}, RequestPath:{}, Controller:{}, Method:{}",
                        pathInfo.getRequestMethod(), pathInfo.getRequestPath(),
                        controllerInfo.getControllerClass().getName(), controllerInfo.getInvokeMethod().getName());
            }
        }
    }

}
