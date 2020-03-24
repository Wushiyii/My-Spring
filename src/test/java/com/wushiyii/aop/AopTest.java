package com.wushiyii.aop;

import com.wushiyii.controller.TestController;
import com.wushiyii.core.BeanContainer;
import com.wushiyii.ioc.IocHandler;
import org.junit.Test;

public class AopTest {

    @Test
    public void doAop() {
        BeanContainer container = BeanContainer.getInstance();
        container.loadBeans("com.wushiyii");
        new AopHandler().init();
        new IocHandler().init();

        TestController testController = (TestController) container.getClassInstance(TestController.class);
        testController.returnFun();
    }

}
