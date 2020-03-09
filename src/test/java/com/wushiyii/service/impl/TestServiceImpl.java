package com.wushiyii.service.impl;

import com.wushiyii.annotation.mvc.Service;
import com.wushiyii.service.TestService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wgq
 * @date 2020/3/9 4:42 下午
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Override
    public void fun() {
        log.info("fun() method execute");
    }
}
