package cn.mybatis.mp.core.mybatis;

import java.lang.reflect.Method;

public class MethodUtil {

    public static Method getMethod(Class<?> clazz, String methodName) {
        Method method = getMethod(clazz.getMethods(), methodName);
        if (method == null) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                method = getMethod(superClass, methodName);
                if (method != null) {
                    return method;
                }
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces != null && interfaces.length > 0) {
                for (Class c : clazz.getInterfaces()) {
                    method = getMethod(c.getMethods(), methodName);
                    if (method != null) {
                        break;
                    }
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
