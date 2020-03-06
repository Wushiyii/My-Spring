package com.wushiyii.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

/**
 * @author wushiyii
 * @date 2020/3/2 4:01 下午
 */
@Slf4j
public class ClassUtil {

    public static ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("get class error, e:", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        try {
            return (T) getClassByName(className).newInstance();
        } catch (Exception e) {
            log.error("new instance error, e:", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            log.error("new instance error, e:", e);
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object target, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (Exception e) {
            log.error("set field error, e:", e);
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getClassByPath(Path classPath, Path basePath, String basePackage) {
        String packageName = classPath.toString().replace(basePath.toString(), "");
        final String className = (basePackage + packageName)
                .replace("/", ".")
                .replace("\\", ".")
                .replace(".class", "");
        return getClassByName(className);
    }

    public static Class<?> getClassByJar(JarEntry entry) {
        final String jarEntryName = entry.getName();
        final String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
                .replace("/", ".");
        return getClassByName(className);
    }


    public static Set<Class<?>> getPackageClass(String packagePath) {
        URL url = getCurrentClassLoader().getResource(packagePath.replace(".", "/"));
        if (Objects.isNull(url)) {
            throw new RuntimeException("无法获取包路径文件");
        }
        try {
            if ("file".equalsIgnoreCase(url.getProtocol())) {
                File file = new File(url.getPath());
                Path bathPath = file.toPath();
                return Files.walk(bathPath)
                        .filter(path -> path.toFile().getName().endsWith(".class"))
                        .map(path -> getClassByPath(path, bathPath, packagePath))
                        .collect(Collectors.toSet());
            } else if ("jar".equalsIgnoreCase(url.getProtocol())) {
                final JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                return jarURLConnection.getJarFile()
                        .stream()
                        .filter(entry -> entry.getName().endsWith(".class"))
                        .map(ClassUtil::getClassByJar)
                        .collect(Collectors.toSet());
            }
            return Collections.emptySet();
        } catch (IOException e) {
            log.error("load package error", e);
            throw new RuntimeException(e);
        }
    }
}
