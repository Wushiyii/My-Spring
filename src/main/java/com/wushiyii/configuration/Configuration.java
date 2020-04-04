package com.wushiyii.configuration;

import lombok.Builder;
import lombok.Data;

/**
 * @author wgq
 * @date 2020/4/4 8:55 下午
 */

@Data
@Builder
public class Configuration {

    /**
     * 启动类
     */
    private Class<?> bootClass;

    /**
     * 资源默认目录
     */
    @Builder.Default
    private String resourcePath = "src/main/resources/";

    /**
     * 端口号
     */
    @Builder.Default
    private int serverPort = 8088;

    /**
     * tomcat docBase目录
     */
    @Builder.Default
    private String docBase;

    /**
     * tomcat contextPath目录
     */
    @Builder.Default
    private String contextPath;

}
