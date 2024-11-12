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
            return null;
        }

        if (methodName.contains("&")) {
            return null;
        }

        Method mapperMethod = getMethod(mapperClass, methodName);
        if (mapperMethod == null) {
            for (Class clazz : mapperClass.getInterfaces()) {
                mapperMethod = getMethod(clazz, methodName);
                if (mapperMethod != null) {
                    break;
                }
            }
        }
        return mapperMethod;
    }

    private static Method getMethod(Class<?> clazz, String methodName) {
        Method method = getMethod(clazz.getMethods(), methodName);
        if (method == null) {
            for (Class c : clazz.getInterfaces()) {
                method = getMethod(c.getMethods(), methodName);
                if (method != null) {
                    break;
                }
            }
        }
        return method;
    }

    private static Method getMethod(Method[] methods, String methodName) {
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }
}
