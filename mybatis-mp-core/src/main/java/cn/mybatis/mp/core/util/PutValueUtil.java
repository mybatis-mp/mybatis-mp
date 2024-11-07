package cn.mybatis.mp.core.util;

import cn.mybatis.mp.core.db.reflect.PutValueInfo;
import cn.mybatis.mp.db.annotations.PutValue;
import db.sql.api.impl.tookit.Objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public final class PutValueUtil {
    private static final Object NULL_VALUE = new Object();

    public static Object getPutValue(Object[] values, PutValueInfo putValueInfo, Map<String, Object> sessionCache) {
        String cacheKey = Arrays.toString(values) + "." + putValueInfo.getFactory().getName() + "." + putValueInfo.getAnnotation().method();
        Object v = sessionCache.computeIfAbsent(cacheKey, key -> {
            Object value = getPutValue(values, putValueInfo);
            return value == null ? NULL_VALUE : value;
        });
        return v == NULL_VALUE ? null : v;
    }

    public static Object getPutValue(Object[] values, PutValueInfo putValueInfo) {
        PutValue annotation = putValueInfo.getAnnotation();
        boolean allNull = !Arrays.stream(values).filter(java.util.Objects::nonNull).findFirst().isPresent();
        if (allNull) {
            if (annotation.required()) {
                throw new RuntimeException(" values are all null from table");
            }
            return putValueInfo.getDefaultValue();
        }

        Class<?>[] paramTypes = Arrays.stream(values).map(Object::getClass).toArray(Class[]::new);
        try {
            Method putValueMethod = annotation.factory().getMethod(annotation.method(), paramTypes);
            putValueMethod.setAccessible(true);
            Object value = putValueMethod.invoke(null, values);
            if (Objects.isNull(value)) {
                if (annotation.required()) {
                    throw new RuntimeException(" value is  null from " + annotation.factory() + " " + annotation.method());
                } else {
                    return putValueInfo.getDefaultValue();
                }
            }
            return TypeConvertUtil.convert(value, putValueInfo.getFieldInfo().getTypeClass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
