package cn.mybatis.mp.core.util;

import cn.mybatis.mp.db.annotations.OnValue;
import db.sql.api.impl.tookit.Objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnValueUtil {

    private final static Map<String, Method> ON_METHOD_CACHE = new ConcurrentHashMap<>();

    public static void onValue(Object rowValue, OnValue onValue) {
        if (Objects.isNull(rowValue)) {
            return;
        }

        Method method = ON_METHOD_CACHE.computeIfAbsent(rowValue.getClass().getName() + onValue.value().getName(), key -> {
            try {
                return onValue.value().getMethod("onValue", rowValue.getClass());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });


        try {
            method.invoke(null, rowValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
