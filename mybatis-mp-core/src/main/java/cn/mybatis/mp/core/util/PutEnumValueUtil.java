package cn.mybatis.mp.core.util;

import cn.mybatis.mp.core.db.reflect.PutEnumValueInfo;
import cn.mybatis.mp.db.annotations.PutEnumValue;
import db.sql.api.impl.tookit.Objects;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PutEnumValueUtil {

    private static final Map<String, Object> ENUM_VALUE_CACHE = new ConcurrentHashMap<>();

    private static final Object NULL_VALUE = new Object();

    public static Object getEnumValue(Object code, PutEnumValueInfo putEnumValueInfo) {

        Object value = ENUM_VALUE_CACHE.computeIfAbsent(putEnumValueInfo.getAnnotation().target().getName() + "." + code, key -> {
            PutEnumValue annotation = putEnumValueInfo.getAnnotation();
            if (!putEnumValueInfo.getAnnotation().target().isEnum()) {
                throw new RuntimeException(annotation.target().getName() + " is not an enum");
            }

            if (Objects.isNull(code)) {
                if (annotation.required()) {
                    throw new RuntimeException("code value is not null from table");
                }
                return NULL_VALUE;
            }

            Object[] enums = annotation.target().getEnumConstants();
            Field codeField;
            Field valueField;
            try {
                codeField = annotation.target().getDeclaredField(annotation.code());
                valueField = annotation.target().getDeclaredField(annotation.value());

                codeField.setAccessible(true);
                valueField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(annotation.target().getName() + " has no " + e.getMessage() + " field");
            }
            String codeString = code.toString();
            try {
                for (Object e : enums) {
                    if (codeString.equals(codeField.get(e).toString())) {
                        return TypeConvertUtil.convert(valueField.get(e), putEnumValueInfo.getField().getType());
                    }
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }

            return NULL_VALUE;
        });

        if (value == NULL_VALUE) {
            if (putEnumValueInfo.getAnnotation().required()) {
                throw new RuntimeException("code value is not match with " + putEnumValueInfo.getAnnotation().target().getName());
            }
            value = putEnumValueInfo.getDefaultValue();
        }

        return value;
    }
}
