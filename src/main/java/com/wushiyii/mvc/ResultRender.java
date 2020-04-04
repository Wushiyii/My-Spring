package com.wushiyii.mvc;

import com.alibaba.fastjson.JSON;
import com.wushiyii.annotation.mvc.ResponseBody;
import com.wushiyii.core.BeanContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wgq
 * @date 2020/3/31 4:45 下午
 */
@Slf4j
public class ResultRender {

    private BeanContainer beanContainer;

    public ResultRender() {
        beanContainer = BeanContainer.getInstance();
    }

    public void invokeController(ControllerInfo controllerInfo, HttpServletRequest req, HttpServletResponse resp) {
        // 1.获取请求的所有参数
        Map<String, String> requestParams = getRequestOriginParam(req);

        // 2.实例化参数入参
        List<Object> instantiationParam = instantiationMethodArgs(controllerInfo.getMethodParameter(), requestParams);

        // 3.调用控制器方法
        Object controllerBean = beanContainer.getBean(controllerInfo.getControllerClass());
        Method controllerMethod = controllerInfo.getInvokeMethod();
        controllerMethod.setAccessible(true);
        Object returnVal;

        try {
            if (instantiationParam.isEmpty()) {
                returnVal = controllerMethod.invoke(controllerBean);
            } else {
                Object[] args;
                returnVal = controllerMethod.invoke(controllerBean, instantiationParam.toArray());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        handleResult(controllerInfo, returnVal, req, resp);
    }

    private Map<String, String> getRequestOriginParam(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterMap().forEach((name, value) -> {
            if (Objects.nonNull(value) && value.length != 0) {
                paramMap.put(name, value[0]);
            }
        });
        return paramMap;
    }

    private List<Object> instantiationMethodArgs(Map<String, Class<?>> methodParams, Map<String, String> requestParams) {
        return methodParams.keySet().stream().map(paramName -> {
            Class<?> paramType = methodParams.get(paramName);
            String paramValue = requestParams.get(paramName);
            return Objects.isNull(paramValue) ? primitiveNull(paramType) : convert(paramType, paramValue);
        }).collect(Collectors.toList());
    }


    private void handleResult(ControllerInfo controllerInfo, Object returnVal, HttpServletRequest req, HttpServletResponse resp) {
        if (Objects.isNull(returnVal)) {
            return;
        }
        if (controllerInfo.getInvokeMethod().isAnnotationPresent(ResponseBody.class)) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(JSON.toJSONString(returnVal));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("不支持无ResponseBody注解返回内容");
        }
    }




    public static Object convert(Class<?> type, String value) {
        if (isPrimitive(type)) {
            if (StringUtils.isBlank(value)) {
                return primitiveNull(type);
            }

            if (type.equals(int.class) || type.equals(Integer.class)) {
                return Integer.parseInt(value);
            } else if (type.equals(String.class)) {
                return value;
            } else if (type.equals(Double.class) || type.equals(double.class)) {
                return Double.parseDouble(value);
            } else if (type.equals(Float.class) || type.equals(float.class)) {
                return Float.parseFloat(value);
            } else if (type.equals(Long.class) || type.equals(long.class)) {
                return Long.parseLong(value);
            } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                return Boolean.parseBoolean(value);
            } else if (type.equals(Short.class) || type.equals(short.class)) {
                return Short.parseShort(value);
            } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                return Byte.parseByte(value);
            }
            return value;
        } else {
            throw new RuntimeException("暂时不支持非原生");
        }
    }

    /**
     * 返回基本数据类型的空值
     *
     * @param type 类
     * @return 对应的空值
     */
    private static Object primitiveNull(Class<?> type) {
        if (type.equals(int.class) || type.equals(double.class) ||
                type.equals(short.class) || type.equals(long.class) ||
                type.equals(byte.class) || type.equals(float.class)) {
            return 0;
        }
        if (type.equals(boolean.class)) {
            return false;
        }
        return null;
    }


    /**
     * 判定是否基本数据类型(包括包装类)
     *
     * @param type 类
     * @return 是否为基本数据类型
     */
    private static boolean isPrimitive(Class<?> type) {
        return type == boolean.class
                || type == Boolean.class
                || type == double.class
                || type == Double.class
                || type == float.class
                || type == Float.class
                || type == short.class
                || type == Short.class
                || type == int.class
                || type == Integer.class
                || type == long.class
                || type == Long.class
                || type == String.class
                || type == byte.class
                || type == Byte.class
                || type == char.class
                || type == Character.class;
    }

}
