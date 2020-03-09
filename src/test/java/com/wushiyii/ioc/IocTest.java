package com.wushiyii.ioc;

import com.wushiyii.core.BeanContainer;
import com.wushiyii.controller.TestController;
import org.junit.Test;

/**
 * @author wgq
 * @date 2020/3/9 4:44 下午
 */
public class IocTest {

    @Test
    public void initTest() {
        BeanContainer container = BeanContainer.getInstance();
        container.loadBeans("com.wushiyii");
        new IocHandler().init();
        TestController controller = (TestController) container.getBean(TestController.class);
        controller.fun();
    }

}
