package db.sql.api.impl.tookit;

import db.sql.api.Getter;

import java.lang.reflect.Field;

public final class SqlUtil {

    public static final String AS_SPLIT = "$$";

    public static <T> String getAsName(Getter<T> getter) {
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(getter);
        return lambdaFieldInfo.getType().getSimpleName() + AS_SPLIT + lambdaFieldInfo.getName();
    }

    public static String getAsName(Class clazz, Field field) {
        return clazz.getSimpleName() + AS_SPLIT + field.getName();
    }

    public static boolean isAsName(Class clazz, Field field, String name) {
        if (!name.contains(AS_SPLIT)) {
            return false;
        }
        return getAsName(clazz, field).equals(name);
    }
}
