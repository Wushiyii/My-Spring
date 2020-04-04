package com.wushiyii.controller;

import com.wushiyii.annotation.ioc.Autowired;
import com.wushiyii.annotation.mvc.Controller;
import com.wushiyii.annotation.mvc.RequestMapping;
import com.wushiyii.annotation.mvc.ResponseBody;
import com.wushiyii.service.TestService;
import lombok.Data;

/**
 * @author wgq
 * @date 2020/3/9 4:41 下午
 */
@Data
@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;

    public void fun() {
        testService.fun();
    }
    @ResponseBody
    @RequestMapping("returnFun")
    public String returnFun() {
        return testService.returnFun("hello");
    }

}
