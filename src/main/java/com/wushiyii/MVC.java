package com.wushiyii;

import com.wushiyii.aop.AopHandler;
import com.wushiyii.configuration.Configuration;
import com.wushiyii.core.BeanContainer;
import com.wushiyii.ioc.IocHandler;
import com.wushiyii.server.Server;
import com.wushiyii.server.impl.TomcatServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wgq
 * @date 2020/4/4 9:32 下午
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class MVC {

    public static Configuration configuration = Configuration.builder().build();

    public static Server server;

    public static void run(Class<?> bootClass) {
        run(Configuration.builder().bootClass(bootClass).build());
    }

    public static void run(Class<?> bootClass, int port) {
        run(Configuration.builder().bootClass(bootClass).serverPort(port).build());
    }

    public static void run(Configuration configuration) {
        new MVC().start(configuration);
    }

    private void start(Configuration configuration) {
        try {
            MVC.configuration = configuration;
            String basePackageName = configuration.getBootClass().getPackage().getName();
            BeanContainer.getInstance().loadBeans(basePackageName);
            new AopHandler().init();
            new IocHandler().init();
            server = new TomcatServer(configuration);
            server.startServer();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fail to start up , e:", e);
        }
    }
}
