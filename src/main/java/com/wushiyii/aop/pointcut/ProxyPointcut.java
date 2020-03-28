package com.wushiyii.aop.pointcut;

import lombok.Data;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author wgq
 * @date 2020/3/28 9:49 下午
 */
@Data
public class ProxyPointcut {

    /**
     * 切点解析器
     */
    private PointcutParser pointcutParser;

    /**
     * AspectJ表达式
     */
    private String expression;

    /**
     * 表达式解析器
     */
    private PointcutExpression pointcutExpression;

    /**
     * AspectJ语法合集
     */
    private static final Set<PointcutPrimitive> DEFAULT_SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    public ProxyPointcut(Set<PointcutPrimitive> pointcutPrimitives) {
        pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(pointcutPrimitives);
    }

    public ProxyPointcut() {
        this(DEFAULT_SUPPORTED_PRIMITIVES);
    }

    /**
     * 判断class是否匹配表达式
     * @param targetClass  目标class
     * @return 结果
     */
    public boolean matches(Class<?> targetClass) {
        checkReadyToMatch();
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }


    public boolean matches(Method method) {
        checkReadyToMatch();
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);
        if (shadowMatch.alwaysMatches()) {
            return true;
        } else if (shadowMatch.neverMatches()) {
            return false;
        }
        return false;
    }


    private void checkReadyToMatch() {
        if (Objects.isNull(pointcutExpression)) {
            pointcutExpression = pointcutParser.parsePointcutExpression(expression);
        }
    }


}
