package com.wushiyii.util;

import org.junit.Test;

/**
 * @author wgq
 * @date 2020/3/6 10:17 上午
 */
public class ClassUtilTest {

    @Test
    public void getClassByName() throws IllegalAccessException, InstantiationException {
        final Class<?> clazz = ClassUtil.getClassByName("com.wushiyii.mvc.TestController");
        System.out.println(clazz);
        System.out.println(clazz.newInstance());
    }

}
