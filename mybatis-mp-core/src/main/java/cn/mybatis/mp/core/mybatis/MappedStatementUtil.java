package cn.mybatis.mp.core.mybatis;

import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

public final class MappedStatementUtil {

    public static Method getMethod(MappedStatement ms) {
        String methodName;
        Class mapperClass;
        try {
            String mapperName;
            int dot = ms.getId().lastIndexOf(".");
            if (dot == -1) {
                dot = ms.getId().indexOf("@");
                String id = ms.getId().substring(dot + 1);
                dot = id.lastIndexOf("-");
                mapperName = id.substring(0, dot).replaceAll("-", ".");
                methodName = id.substring(dot + 1);
            } else {
                mapperName = ms.getId().substring(0, dot);
                methodName = ms.getId().substring(dot + 1);
            }
            mapperClass = Class.forName(mapperName);
        } catch (Exception e) {
            throw new RuntimeException(ms.getId(), e);
        }

        if (methodName.contains("&")) {
            return null;
        }

        Method mapperMethod = null;
        for (Method method : mapperClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                mapperMethod = method;
                break;
            }
        }
        return mapperMethod;
    }
}
