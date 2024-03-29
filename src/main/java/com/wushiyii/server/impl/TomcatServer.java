package com.wushiyii.server.impl;

import com.wushiyii.MVC;
import com.wushiyii.configuration.Configuration;
import com.wushiyii.mvc.DispatcherServlet;
import com.wushiyii.server.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * @author wgq
 * @date 2020/4/4 9:02 下午
 */
@Slf4j
public class TomcatServer implements Server {

    private Tomcat tomcat;

    public TomcatServer() {
        this(MVC.getConfiguration());
    }

    public TomcatServer(Configuration configuration) {
        try {
            // 设置tomcat
            this.tomcat = new Tomcat();
            tomcat.setBaseDir(configuration.getDocBase());
            tomcat.setPort(configuration.getServerPort());

            File root = getRootFolder(configuration);
            File resourceFile = new File(root.getAbsolutePath(), configuration.getResourcePath());
            if (!resourceFile.exists()) {
                resourceFile = Files.createTempDirectory("default-doc-base").toFile();
            }
            log.info("Tomcat:configuring app with basedir: [{}]", resourceFile.getAbsolutePath());

            StandardContext context = (StandardContext) tomcat.addWebapp(configuration.getContextPath(), resourceFile.getAbsolutePath());
            context.setParentClassLoader(this.getClass().getClassLoader());
            context.setResources(new StandardRoot(context));

            tomcat.addServlet(configuration.getContextPath(), "dispatcherServlet", new DispatcherServlet()).setLoadOnStartup(0);
            context.addServletMappingDecoded("/*", "dispatcherServlet");

        } catch (Exception e) {
            log.error("initial tomcat fail e:", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void startServer() throws Exception {
        tomcat.start();
        String address = tomcat.getServer().getAddress();
        int port = tomcat.getConnector().getPort();
        log.info("tomcat started at: http://{}:{}", address, port);
        tomcat.getServer().await();
    }

    @Override
    public void stopServer() throws Exception {
        tomcat.stop();
    }

    private File getRootFolder(Configuration configuration) {
        try {
            File root;
            String runningJarPath = configuration.getBootClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            log.info("Tomcat:application resolved root folder: [{}]", root.getAbsolutePath());
            return root;
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
