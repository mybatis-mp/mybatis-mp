package cn.mybatis.mp.core.mybatis;

import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

public final class MappedStatementUtil {

    public static Method getMethod(MappedStatement ms) {
        int dot = ms.getId().lastIndexOf(".");
        String mapperName = ms.getId().substring(0, dot);
        String methodName = ms.getId().substring(dot + 1);
        Class mapperClass;
        try {
            mapperClass = Class.forName(mapperName);
        } catch (ClassNotFoundException e) {
            dot = mapperName.lastIndexOf(".");
            mapperName = ms.getId().substring(0, dot);
            methodName = ms.getId().substring(dot + 1);

            try {
                mapperClass = Class.forName(mapperName);
            } catch (ClassNotFoundException e2) {
                return null;
            }
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
