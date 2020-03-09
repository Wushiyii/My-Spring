package com.wushiyii.controller;

import com.wushiyii.annotation.ioc.Autowired;
import com.wushiyii.annotation.mvc.Controller;
import com.wushiyii.core.BeanContainer;
import com.wushiyii.ioc.IocHandler;
import com.wushiyii.service.TestService;
import lombok.Data;

/**
 * @author wgq
 * @date 2020/3/9 4:41 下午
 */
@Data
@Controller
public class TestController {

    @Autowired
    private TestService testService;

    public void fun() {
        testService.fun();
    }

}
