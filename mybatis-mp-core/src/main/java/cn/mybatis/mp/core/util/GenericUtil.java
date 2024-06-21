package cn.mybatis.mp.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericUtil {

    public static List<Class<?>> get(Class clazz) {
        List<Class<?>> list = getGenericInterfaceClass(clazz);
        list.addAll(getGenericSuperClass(clazz));
        return list;
    }

    /**
     * 获取通过继承的泛型类
     *
     * @param clazz
     * @return
     */
    public static List<Class<?>> getGenericSuperClass(Class clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            List<Class<?>> classList = new ArrayList<>(actualTypeArguments.length);
            for (Type actualTypeArgument : actualTypeArguments) {
                if (!(actualTypeArgument instanceof Class)) {
                    continue;
                }
                classList.add((Class) actualTypeArgument);
            }
            return classList;
        }
        return Collections.emptyList();
    }

    /**
     * 获取通过接口实现的返回
     *
     * @param clazz
     * @return
     */
    public static List<Class<?>> getGenericInterfaceClass(Class clazz) {
        Type[] types = clazz.getGenericInterfaces();
        return getGenericTypes(types);
    }

    public static List<Class<?>> getGenericTypes(Type[] types) {
        List<Class<?>> classList = new ArrayList<>(types.length * 2);
        for (Type type : types) {
            classList = getGeneric(type, classList);
        }
        return classList;
    }

    public static List<Class<?>> getGeneric(Type type) {
        return getGeneric(type, new ArrayList<>(1));
    }

    public static List<Class<?>> getGeneric(Type type, List<Class<?>> resultList) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                if (!(actualTypeArgument instanceof Class)) {
                    continue;
                }
                resultList.add((Class) actualTypeArgument);
            }
        }
        return resultList;
    }

    public static List<Class<?>> getGenericParameterTypes(Method method) {
        return getGenericTypes(method.getGenericParameterTypes());
    }
}
